/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash

import android.content.Intent
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.BaseComponentActivity
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.root.RootActivity
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash.presenter.SplashPresenter
import dagger.Lazy
import javax.inject.Inject

class SplashActivity : BaseComponentActivity(), ISplashView {

    @Inject
    lateinit var daggerPresenter: Lazy<SplashPresenter>
    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @ProvidePresenter
    protected fun provideSplashPresenter(): SplashPresenter = daggerPresenter.get()

    override val layoutResId: Int = R.layout.activity_splash

    override fun showNextScreen() {
        val intent = Intent(this, RootActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}