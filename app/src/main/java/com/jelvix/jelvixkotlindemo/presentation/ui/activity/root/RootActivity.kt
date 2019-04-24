/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.activity.root

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.base.BaseComponentActivity
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.root.presenter.RootPresenter
import dagger.Lazy
import javax.inject.Inject

class RootActivity : BaseComponentActivity(), IRootView {

    @Inject
    lateinit var daggerPresenter: Lazy<RootPresenter>
    @InjectPresenter
    lateinit var presenter: RootPresenter

    @ProvidePresenter
    protected fun provideSplashPresenter(): RootPresenter = daggerPresenter.get()

    override val layoutResId: Int = R.layout.activity_root
}