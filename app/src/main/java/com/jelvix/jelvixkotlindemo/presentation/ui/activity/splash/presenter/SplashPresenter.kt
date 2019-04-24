/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash.presenter

import com.arellomobile.mvp.InjectViewState
import com.jelvix.jelvixkotlindemo.presentation.presenter.BasePresenter
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash.ISplashView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@InjectViewState
class SplashPresenter @Inject constructor() : BasePresenter<ISplashView>() {

    companion object {
        const val SPLASH_TIME_SECONDS = 2L
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        showNextScreen()
    }


    /* private functions */

    private fun showNextScreen() {
        addDisposable(Single.timer(SPLASH_TIME_SECONDS, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.showNextScreen()
            }, this::handleError))
    }
}