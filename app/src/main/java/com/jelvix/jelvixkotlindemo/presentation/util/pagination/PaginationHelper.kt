/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.pagination

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jelvix.jelvixkotlindemo.data.api.ApiConstants
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.lang.ref.WeakReference

class PaginationHelper {

    companion object {
        const val SCROLL_THRESHOLD = (ApiConstants.DEFAULT_LIMIT / 2) + 1
    }

    protected lateinit var scrollLoadingChannel: PublishSubject<Any>
    protected var subscribeToLoadingChannelDisposable: Disposable? = null
    protected lateinit var paginationInfo: PaginationInfo
    protected var recyclerViewWeakReference: WeakReference<RecyclerView>? = null
    protected var swipeRefreshLayoutWeakReference: WeakReference<SwipeRefreshLayout>? = null
    protected var adapter: RecyclerView.Adapter<*>? = null
        get() = recyclerViewWeakReference?.get()?.adapter
    protected val onLoadComplete: (Boolean) -> Unit = {
        getAutoLoadingAdapter()?.enableFooter(false)
        swipeRefreshLayoutWeakReference?.get()?.isRefreshing = false
        if (!paginationInfo.isAllPortionsLoaded) {
            subscribeToLoadingChannel()
        }
    }
    private val scrollingChannelListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            checkIsNeedToLoadMoreData()
        }
    }

    fun initialize(recyclerView: RecyclerView, swipeRefreshLayout: SwipeRefreshLayout? = null) {
        recyclerViewWeakReference = WeakReference(recyclerView)
        swipeRefreshLayout?.let {
            swipeRefreshLayoutWeakReference = WeakReference(it)
        }
    }

    fun release() {
        unSubscribe()
        recyclerViewWeakReference?.clear()
        swipeRefreshLayoutWeakReference?.clear()
    }

    /**
     * required method
     * call after initialize all parameters in PaginationHelper
     */
    fun startLoading(paginationInfo: PaginationInfo) {
        paginationInfo.onLoadComplete = onLoadComplete
        this.paginationInfo = paginationInfo
        restart()

        // if all data was loaded then new download is not needed
        if (paginationInfo.isAllPortionsLoaded) {
            return
        }
        // if data are loading, we are waiting an onLoadComplete callback
        if (paginationInfo.isLoading && !paginationInfo.isRefreshingEnabled) {
            getAutoLoadingAdapter()?.enableFooter(true)
            return
        }
        // if first portion was loaded then subscribe to LoadingChannel, otherwise we loading first data portion
        if (paginationInfo.isFirstPortionLoaded) {
            subscribeToLoadingChannel()
        } else {
            paginationInfo.onNext(paginationInfo.page)
            if (!paginationInfo.isRefreshingEnabled) {
                getAutoLoadingAdapter()?.enableFooter(true)
            }
        }
    }

    fun reset(isRefreshingEnabled: Boolean = true) {
        paginationInfo.refreshState = paginationInfo.clone()
        paginationInfo.reset(isRefreshingEnabled)
        startLoading(paginationInfo)
    }

    fun unSubscribe() {
        adapter = null
        scrollLoadingChannel.onComplete()
        subscribeToLoadingChannelDisposable?.dispose()
    }

    fun restart() {
        recyclerViewWeakReference?.get()?.removeOnScrollListener(scrollingChannelListener)
        scrollLoadingChannel = PublishSubject.create<Any>()
        if (paginationInfo.isRefreshingEnabled) {
            getAutoLoadingAdapter()?.enableFooter(false)
        }
        startScrollingChannel()
    }

    fun checkRefreshFinishing() {
        if (paginationInfo.isRefreshingEnabled) {
            checkIsNeedToLoadMoreData()
            getAutoLoadingAdapter()?.clearItems()
            paginationInfo.isRefreshingEnabled = false
        }
    }

    protected fun startScrollingChannel() {
        recyclerViewWeakReference?.get()?.addOnScrollListener(scrollingChannelListener)
    }

    private fun getAutoLoadingAdapter() = adapter as? AutoLoadingRecyclerViewAdapter<*, *>

    private fun checkIsNeedToLoadMoreData() {
        val position = getLastVisibleItemPosition()
        val updatePosition = (adapter?.itemCount ?: 0) - SCROLL_THRESHOLD
        if (position >= updatePosition && !paginationInfo.isAllPortionsLoaded && !paginationInfo.isLoading) {
            scrollLoadingChannel.onNext(Any())
        }
    }

    fun getLastVisibleItemPosition(): Int {
        val layoutManager = recyclerViewWeakReference?.get()?.layoutManager ?: return 0
        return when (layoutManager) {
            is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
            is StaggeredGridLayoutManager -> layoutManager.findLastVisibleItemPositions(null).toList().max() ?: RecyclerView.NO_POSITION
            else -> throw IllegalArgumentException("Unknown LayoutManager class: ${layoutManager.javaClass}")
        }
    }

    protected fun subscribeToLoadingChannel() {
        subscribeToLoadingChannelDisposable = scrollLoadingChannel
                .subscribe({
                    subscribeToLoadingChannelDisposable?.dispose()
                    paginationInfo.onNext(paginationInfo.page)
                    getAutoLoadingAdapter()?.enableFooter(true)
                }, { paginationInfo.doOnError(it) })
    }
}