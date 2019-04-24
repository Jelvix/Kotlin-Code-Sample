/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject

import com.jelvix.jelvixkotlindemo.presentation.ui.activity.root.RootActivity
import com.jelvix.jelvixkotlindemo.presentation.ui.activity.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun provideSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun provideRootActivity(): RootActivity
}