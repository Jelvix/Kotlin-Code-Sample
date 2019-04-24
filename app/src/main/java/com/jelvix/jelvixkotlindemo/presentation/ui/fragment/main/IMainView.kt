/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.presentation.presenter.IView

@StateStrategyType(AddToEndSingleStrategy::class)
interface IMainView : IView {

    fun onPictureOfTheDayLoaded(mediaItem: MediaItem)

    fun onMediaImageTick(mediaItem: MediaItem)

    fun onMediaVideoTick(mediaItem: MediaItem)

    fun onMediaFavoriteTick(mediaItem: MediaItem)

    fun onTotalCountLoaded(totalCount: Int, screenType: ScreenType)
}