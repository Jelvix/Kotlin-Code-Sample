/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.pagination

class PaginationInfo(
    var onNext: (maxId: Int?) -> Unit,
    var doOnError: (Throwable) -> Unit
) : Cloneable {

    var page: Int? = 1
    var isFirstPortionLoaded: Boolean = false
    var isAllPortionsLoaded: Boolean = false
    var isLoading:Boolean = false
    var onLoadComplete: (isSuccessful: Boolean) -> Unit = {}
    var refreshState: PaginationInfo? = null
    var isRefreshingEnabled = false

    fun reset(isRefreshingEnabled: Boolean = true) {
        page = 1
        isFirstPortionLoaded = false
        isAllPortionsLoaded = false
        isLoading = false
        this.isRefreshingEnabled = isRefreshingEnabled
    }

    public override fun clone(): PaginationInfo {
        return (super.clone() as PaginationInfo)
    }

    fun restoreState(copiedPaginationInfo: PaginationInfo) {
        page = copiedPaginationInfo.page
        isFirstPortionLoaded = copiedPaginationInfo.isFirstPortionLoaded
        isAllPortionsLoaded = copiedPaginationInfo.isAllPortionsLoaded
        isLoading = copiedPaginationInfo.isLoading
        isRefreshingEnabled = false
        refreshState = null
    }
}