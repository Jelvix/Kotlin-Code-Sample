/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject

import com.jelvix.jelvixkotlindemo.presentation.inject.module.*
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail.MediaDetailFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main.MainFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector(modules = [NasaInteractorModule::class, DbInteractorModule::class, ApodInteractorModule::class])
    abstract fun provideMainFragment(): MainFragment

    @ContributesAndroidInjector(modules = [NasaInteractorModule::class, DbInteractorModule::class, SearchFragmentModule::class])
    abstract fun provideSearchFragment(): SearchFragment

    @ContributesAndroidInjector(modules = [NasaInteractorModule::class, DbInteractorModule::class, MediaDetailFragmentModule::class])
    abstract fun provideMediaDetailFragment(): MediaDetailFragment
}