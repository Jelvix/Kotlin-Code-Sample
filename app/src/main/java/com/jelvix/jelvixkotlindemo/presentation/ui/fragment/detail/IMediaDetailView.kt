/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jelvix.jelvixkotlindemo.data.entity.OriginMedia
import com.jelvix.jelvixkotlindemo.presentation.presenter.IView

interface IMediaDetailView : IView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onOriginMediaLoaded(originMedia: OriginMedia)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun notifyOptionsMenu()
}