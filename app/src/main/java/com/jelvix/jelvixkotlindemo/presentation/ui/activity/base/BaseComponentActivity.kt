/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.base

import android.os.Bundle
import android.view.Menu
import androidx.fragment.app.Fragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jelvix.jelvixkotlindemo.BuildConfig
import com.jelvix.jelvixkotlindemo.data.api.exception.ApiException
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.presenter.BaseActivityPresenter
import com.jelvix.jelvixkotlindemo.presentation.util.alert.Alert
import com.jelvix.jelvixkotlindemo.presentation.util.alert.AlertDialogHelper
import com.jelvix.jelvixkotlindemo.presentation.util.progress.ProgressDialogHelper
import dagger.Lazy
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasFragmentInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

abstract class BaseComponentActivity : MvpAppCompatActivity(), IBaseView, HasFragmentInjector,
    HasSupportFragmentInjector {

    protected val progressDialogHelper by lazy { ProgressDialogHelper(this) }
    protected val alertDialogHelper by lazy { AlertDialogHelper(this) }

    @Inject lateinit var daggerBaseActivityPresenter: Lazy<BaseActivityPresenter>
    @InjectPresenter lateinit var baseActivityPresenter: BaseActivityPresenter
    @ProvidePresenter fun provideBaseActivityPresenter(): BaseActivityPresenter = daggerBaseActivityPresenter.get()
    @Inject lateinit var supportFragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<android.app.Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = supportFragmentInjector
    override fun fragmentInjector(): AndroidInjector<android.app.Fragment> = fragmentInjector

    protected open var optionsMenu: Int = -1
    abstract val layoutResId: Int


    /* lifecycle */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (optionsMenu != -1) {
            menuInflater.inflate(optionsMenu, menu)
            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layoutResId)
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialogHelper.dismissDialog(true)
        progressDialogHelper.dismissDialog()
    }


    /* public functions */

    override fun handleError(throwable: Throwable) {
        val alertBuilder = Alert.newBuilder()
        alertBuilder.setType(Alert.ERROR)
        when (throwable) {
            is ApiException -> {
                alertBuilder.setMessage(throwable.message ?: throwable.errorDetails.body)
            }
            is Exception -> {
                alertBuilder.setMessage(throwable.message)
                if (BuildConfig.DEBUG) {
                    throwable.printStackTrace()
                }
            }
            else -> {
                if (BuildConfig.DEBUG) {
                    throwable.printStackTrace()
                }
            }
        }
        alertDialogHelper.showAlertDialog(alertBuilder.create())
    }

    override fun setAlertDismissListener(doOnDismiss: (() -> Unit)?) {
        alertDialogHelper.doOnDismiss = doOnDismiss
    }

    override fun showAlertDialog(message: String) = alertDialogHelper.showAlertDialog(message, null)

    override fun showAlertDialog(alert: Alert) = alertDialogHelper.showAlertDialog(alert)

    override fun hideAlertDialog() = alertDialogHelper.dismissDialog()

    override fun showProgressDialog(message: String?)= progressDialogHelper.showProgressDialog(message)

    override fun hideProgressDialog() = progressDialogHelper.dismissDialog()
}