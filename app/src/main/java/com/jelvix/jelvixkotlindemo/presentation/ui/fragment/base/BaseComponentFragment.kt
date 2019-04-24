/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jelvix.jelvixkotlindemo.presentation.util.alert.Alert
import com.jelvix.jelvixkotlindemo.presentation.presenter.IView
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.BaseComponentActivity
import com.jelvix.jelvixkotlindemo.presentation.util.ext.hideKeyboard
import dagger.android.support.AndroidSupportInjection

abstract class BaseComponentFragment : MvpAppCompatFragment(), IView {

    abstract val layoutResId: Int


    /* lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutResId, container, false)
    }

    override fun onDestroyView() {
        activity?.hideKeyboard()
        super.onDestroyView()
    }


    /* public functions */

    override fun setAlertDismissListener(doOnDismiss: (() -> Unit)?) = getBaseComponentActivity().setAlertDismissListener(doOnDismiss)

    override fun handleError(throwable: Throwable) = getBaseComponentActivity().handleError(throwable)

    override fun showAlertDialog(message: String) = getBaseComponentActivity().showAlertDialog(message)

    override fun showAlertDialog(alert: Alert) = getBaseComponentActivity().showAlertDialog(alert)

    override fun hideAlertDialog() = getBaseComponentActivity().hideAlertDialog()

    override fun showProgressDialog(message: String?) = getBaseComponentActivity().showProgressDialog(message)

    override fun hideProgressDialog() = getBaseComponentActivity().hideProgressDialog()

    private fun getBaseComponentActivity(): BaseComponentActivity {
        if (activity is BaseComponentActivity) {
            return activity as BaseComponentActivity
        } else {
            throw RuntimeException("BaseComponentFragment must be used in BaseComponentActivity")
        }
    }

}