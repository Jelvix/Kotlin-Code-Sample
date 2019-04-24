/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.data.entity.Category
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.domain.IApodInteractor
import com.jelvix.jelvixkotlindemo.domain.IDbInteractor
import com.jelvix.jelvixkotlindemo.domain.INasaInteractor
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main.IMainView
import com.jelvix.jelvixkotlindemo.presentation.util.bus.MediaItemAddedToFavorites
import com.jelvix.jelvixkotlindemo.presentation.util.bus.MediaItemRemovedFromFavorites
import com.jelvix.jelvixkotlindemo.presentation.util.bus.RxBus
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class MainPresenter @Inject constructor(
    private val nasaInteractor: INasaInteractor,
    private val dbInteractor: IDbInteractor,
    private val apodInteractor: IApodInteractor
) : BasePresenter<IMainView>() {

    val categories = mutableListOf(
        Category(ScreenType.APOD),
        Category(ScreenType.IMAGE),
        Category(ScreenType.VIDEO),
        Category(ScreenType.FAVORITE)
    )

    val initialMediaImages = mutableListOf<MediaItem>()
    val initialMediaVideos = mutableListOf<MediaItem>()
    val initialMediaFavorites = mutableListOf<MediaItem>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        fetchMediaImageData()
        fetchMediaVideoData()
        fetchMediaFavoriteData()
        subscribeToRxBus()
        addDisposable(apodInteractor.getPictureOfTheDay()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.onPictureOfTheDayLoaded(it!!)
            }, this::handleError))
    }


    /* private functions */

    private fun fetchMediaImageData() {
        addDisposable(nasaInteractor.searchMediaWithTotalCount(null, null, MediaType.IMAGE)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (medias, totalCount) ->
                viewState.onTotalCountLoaded(totalCount, ScreenType.IMAGE)
                initialMediaImages.addAll(medias.take(20))
                startMediaTicker(ScreenType.IMAGE)
            }, this::handleError)
        )
    }

    private fun fetchMediaVideoData() {
        addDisposable(nasaInteractor.searchMediaWithTotalCount(null, null, MediaType.VIDEO)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (medias, totalCount) ->
                viewState.onTotalCountLoaded(totalCount, ScreenType.VIDEO)
                initialMediaVideos.addAll(medias.take(20))
                startMediaTicker(ScreenType.VIDEO)
            }, this::handleError)
        )
    }

    private fun fetchMediaFavoriteData(isWithStartTicker: Boolean = true) {
        addDisposable(dbInteractor.getMediaItemsWithTotalCount(null, null)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (medias, totalCount) ->
                viewState.onTotalCountLoaded(totalCount, ScreenType.FAVORITE)
                initialMediaFavorites.clear()
                initialMediaFavorites.addAll(medias.take(20))
                if (isWithStartTicker) {
                    startMediaTicker(ScreenType.FAVORITE)
                }
            }, this::handleError)
        )
    }

    private fun startMediaTicker(screenType: ScreenType) {
        addDisposable(Flowable.interval(0, 10, TimeUnit.SECONDS)
            .filter {
                when (screenType) {
                    ScreenType.APOD -> 0
                    ScreenType.IMAGE -> initialMediaImages.size
                    ScreenType.VIDEO -> initialMediaVideos.size
                    ScreenType.FAVORITE -> initialMediaFavorites.size
                } != 0
            }
            .map {
                when (screenType) {
                    ScreenType.APOD -> MediaItem()
                    ScreenType.IMAGE -> initialMediaImages[it.toInt() % initialMediaImages.size]
                    ScreenType.VIDEO -> initialMediaVideos[it.toInt() % initialMediaVideos.size]
                    ScreenType.FAVORITE -> initialMediaFavorites[it.toInt() % initialMediaFavorites.size]
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ mediaItem ->
                when(screenType) {
                    ScreenType.IMAGE -> viewState.onMediaImageTick(mediaItem)
                    ScreenType.VIDEO -> viewState.onMediaVideoTick(mediaItem)
                    ScreenType.FAVORITE -> viewState.onMediaFavoriteTick(mediaItem)
                    else -> {}
                }
            }, this::handleError)
        )
    }

    private fun subscribeToRxBus() {
        addDisposable(RxBus.getEvents()
            .subscribe({ event ->
                when(event) {
                    is MediaItemRemovedFromFavorites,
                    is MediaItemAddedToFavorites -> {
                        fetchMediaFavoriteData(false)
                    }
                }
            }, this::handleError))
    }
}