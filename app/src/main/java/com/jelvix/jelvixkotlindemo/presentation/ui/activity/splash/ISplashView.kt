/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jelvix.jelvixkotlindemo.presentation.presenter.IView

interface ISplashView : IView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showNextScreen()
}