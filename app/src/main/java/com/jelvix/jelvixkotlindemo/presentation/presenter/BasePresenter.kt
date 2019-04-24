/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.presenter

import androidx.annotation.CallSuper
import com.arellomobile.mvp.MvpPresenter
import com.jelvix.jelvixkotlindemo.presentation.util.alert.Alert
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<View : IView> : MvpPresenter<View>() {

    private var compositeDisposable: CompositeDisposable? = CompositeDisposable()


    /* lifecycle */

    @CallSuper
    override fun onDestroy() {
        clear()
        compositeDisposable = null
    }


    /* public functions */

    fun showProgressDialog(message: String? = null) = viewState.showProgressDialog(message)

    fun hideProgressDialog() = viewState.hideProgressDialog()

    fun handleError(throwable: Throwable) {
        viewState.setAlertDismissListener(this::hideAlertDialog)
        viewState.handleError(throwable)
    }

    fun showAlertDialog(alert: Alert) {
        viewState.setAlertDismissListener(this::hideAlertDialog)
        viewState.showAlertDialog(alert)
    }

    fun showAlertDialog(message: String) {
        viewState.setAlertDismissListener(this::hideAlertDialog)
        viewState.showAlertDialog(message)
    }

    fun hideAlertDialog() {
        viewState.setAlertDismissListener(null)
        viewState.hideAlertDialog()
    }


    /* rx functions */

    protected fun clear() = compositeDisposable?.clear()
    protected fun addDisposable(disposable: Disposable) = compositeDisposable?.add(disposable)

}