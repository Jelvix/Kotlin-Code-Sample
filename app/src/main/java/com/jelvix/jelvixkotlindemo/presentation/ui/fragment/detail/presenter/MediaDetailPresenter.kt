/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.domain.IDbInteractor
import com.jelvix.jelvixkotlindemo.domain.INasaInteractor
import com.jelvix.jelvixkotlindemo.presentation.inject.module.MediaDetailFragmentModule
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail.IMediaDetailView
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject
import javax.inject.Named

@InjectViewState
class MediaDetailPresenter @Inject constructor(
    private val nasaInteractor: INasaInteractor,
    private val dbInteractor: IDbInteractor,
    @Named(MediaDetailFragmentModule.QUALIFIER_MEDIA)
    var mediaItem: MediaItem
) : BasePresenter<IMediaDetailView>() {

    var isItemInFavorites: Boolean? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (mediaItem.originMedia == null) {
            loadOriginMedia(mediaItem.nasaId)
        } else {
            mediaItem.originMedia?.let { viewState.onOriginMediaLoaded(it) }
            checkIsInFavorite()
        }
    }


    /* public functions */

    fun addMediaItemToFavorites() {
        addDisposable(dbInteractor.insertMediaItem(mediaItem)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isItemInFavorites = true
                viewState.notifyOptionsMenu()
            }, this::handleError))
    }

    fun removeMediaItemFromFavorites() {
        addDisposable(dbInteractor.removeMediaItem(mediaItem)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isItemInFavorites = false
                viewState.notifyOptionsMenu()
            }, this::handleError))
    }


    /* private functions */

    private fun loadOriginMedia(nasaId: String?) {
        addDisposable(nasaInteractor.getOriginMedia(nasaId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ originMedia ->
                mediaItem.originMedia = originMedia
                viewState.onOriginMediaLoaded(originMedia)
                checkIsInFavorite()
            }, this::handleError))
    }

    private fun checkIsInFavorite() {
        addDisposable(dbInteractor.isMediaItemInFavorites(mediaItem)
            .subscribe({
                isItemInFavorites = it
                viewState.notifyOptionsMenu()
            }, this::handleError))
    }
}