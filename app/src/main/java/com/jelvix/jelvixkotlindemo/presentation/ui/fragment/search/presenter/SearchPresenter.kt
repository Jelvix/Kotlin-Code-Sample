/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.domain.IDbInteractor
import com.jelvix.jelvixkotlindemo.domain.INasaInteractor
import com.jelvix.jelvixkotlindemo.presentation.inject.module.SearchFragmentModule
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search.ISearchView
import com.jelvix.jelvixkotlindemo.presentation.util.bus.MediaItemRemovedFromFavorites
import com.jelvix.jelvixkotlindemo.presentation.util.bus.RxBus
import com.jelvix.jelvixkotlindemo.presentation.util.pagination.IPaginablePresenter
import com.jelvix.jelvixkotlindemo.presentation.util.pagination.PaginationInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

@InjectViewState
class SearchPresenter @Inject constructor(
    private val nasaInteractor: INasaInteractor,
    private val dbInteractor: IDbInteractor,
    @Named(SearchFragmentModule.QUALIFIER_SCREEN_TYPE)
    val screenType: ScreenType,
    @Named(SearchFragmentModule.QUALIFIER_INITIAL_DATA)
    private val initialData: List<MediaItem>?
) : BasePresenter<ISearchView>(), IPaginablePresenter {

    override var paginationInfo: PaginationInfo = PaginationInfo(this::loadData, this::handleError)
    val items: MutableList<MediaItem> = mutableListOf()
    var query: String? = null

    private val searchTextSubject = PublishSubject.create<String>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        if (initialData != null) {
            items.addAll(initialData)
        }
        subscribeToSearchTextChangeSubject()
        subscribeToRxBus()
    }

    override fun resetPagination() {
        viewState.onResetPagination()
    }


    /* public functions */

    fun sendNewQuery(query: String) {
        searchTextSubject.onNext(query)
    }


    /* private functions */

    private fun loadData(page: Int?) {
        when(screenType) {
            ScreenType.APOD -> {}
            ScreenType.IMAGE -> loadDataFromNasaApi(page, MediaType.IMAGE)
            ScreenType.VIDEO -> loadDataFromNasaApi(page, MediaType.VIDEO)
            ScreenType.FAVORITE -> {
                addDisposable(dbInteractor.getMediaItems(query, page)
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(applyPaginationProgress())
                    .subscribe({
                        viewState.onDataLoaded(it)
                    }, this::handleError))
            }
        }
    }

    private fun loadDataFromNasaApi(page: Int?, mediaType: MediaType) {
        addDisposable(nasaInteractor.searchMedia(page, query, mediaType)
            .observeOn(AndroidSchedulers.mainThread())
            .compose(applyPaginationProgress())
            .subscribe({
                viewState.onDataLoaded(it)
            }, this::handleError))
    }

    private fun subscribeToSearchTextChangeSubject() {
        addDisposable(searchTextSubject.retry()
            .debounce(1, TimeUnit.SECONDS)
            .filter { it != query ?: "" }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                query = if (it.isEmpty()) null else it
                viewState.restartPagination()
            }, this::handleError))
    }

    private fun subscribeToRxBus() {
        addDisposable(
            RxBus.getEvents()
                .subscribe({ event ->
                    when(event) {
                        is MediaItemRemovedFromFavorites -> {
                            if (screenType == ScreenType.FAVORITE) {
                                viewState.onMediaItemDeleted(event.mediaItem)
                            }
                        }
                    }
                }, this::handleError))
    }
}