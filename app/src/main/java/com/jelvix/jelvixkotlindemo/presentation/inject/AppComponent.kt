/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject

import android.app.Application
import com.jelvix.jelvixkotlindemo.presentation.JelvixDemoKotlinApp
import com.jelvix.jelvixkotlindemo.presentation.inject.module.ApiModule
import com.jelvix.jelvixkotlindemo.presentation.inject.module.AppModule
import com.jelvix.jelvixkotlindemo.presentation.inject.module.DbModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AndroidInjectionModule::class,
    ActivityModule::class,
    FragmentModule::class,
    AppModule::class,
    ApiModule::class,
    DbModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: JelvixDemoKotlinApp)

}
