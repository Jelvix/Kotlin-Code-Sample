/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.presentation.presenter.IView

interface ISearchView : IView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onDataLoaded(items: List<MediaItem>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onMediaItemDeleted(item: MediaItem)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun restartPagination()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onResetPagination()
}