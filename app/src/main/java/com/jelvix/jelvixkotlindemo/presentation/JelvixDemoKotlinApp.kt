/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation

import com.arellomobile.mvp.MvpFacade
import com.jelvix.jelvixkotlindemo.presentation.inject.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class JelvixDemoKotlinApp : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        MvpFacade.init()
    }
}