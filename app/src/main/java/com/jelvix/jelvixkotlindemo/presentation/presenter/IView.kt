/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.presenter

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.jelvix.jelvixkotlindemo.presentation.util.alert.Alert
import com.jelvix.jelvixkotlindemo.presentation.presenter.strategy.AlertStrategy
import com.jelvix.jelvixkotlindemo.presentation.presenter.strategy.ProgressDialogStrategy
import com.jelvix.jelvixkotlindemo.presentation.util.AppConstants

interface IView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setAlertDismissListener(doOnDismiss: (() -> Unit)?)

    @StateStrategyType(AlertStrategy::class)
    fun handleError(throwable: Throwable)

    @StateStrategyType(AlertStrategy::class)
    fun showAlertDialog(message: String)

    @StateStrategyType(AlertStrategy::class)
    fun showAlertDialog(alert: Alert)

    @StateStrategyType(value = AlertStrategy::class, tag = AppConstants.ACTION_HIDE)
    fun hideAlertDialog()

    @StateStrategyType(ProgressDialogStrategy::class)
    fun showProgressDialog(message: String? = null)

    @StateStrategyType(value = ProgressDialogStrategy::class, tag = AppConstants.ACTION_HIDE)
    fun hideProgressDialog()

}