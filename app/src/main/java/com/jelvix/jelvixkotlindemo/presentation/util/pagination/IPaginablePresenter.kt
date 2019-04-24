/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.pagination

import com.jelvix.jelvixkotlindemo.data.api.ApiConstants
import io.reactivex.SingleTransformer

interface IPaginablePresenter {

    var paginationInfo: PaginationInfo

    fun <T> applyPaginationProgress(): SingleTransformer<T, T> {
        return SingleTransformer {
            it.doOnSubscribe {
                paginationInfo.isLoading = true
            }
            .doOnSuccess {
                it as MutableList<*>
                paginationInfo.page = if (it.size < ApiConstants.DEFAULT_LIMIT) null else paginationInfo.page?.plus(1)
                paginationInfo.isFirstPortionLoaded = true
                if (paginationInfo.page == null) {
                    paginationInfo.isAllPortionsLoaded = true
                }
                paginationInfo.onLoadComplete(true)
                paginationInfo.isLoading = false
                paginationInfo.refreshState = null
                if (paginationInfo.isRefreshingEnabled) {
                    resetPagination()
                }
            }
            .doOnError {
                paginationInfo.refreshState?.let {
                    paginationInfo.restoreState(it)
                }
                paginationInfo.onLoadComplete(false)
                paginationInfo.isLoading = false
            }
        }
    }

    fun resetPagination()
}