/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject.module

import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail.MediaDetailFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search.SearchFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class MediaDetailFragmentModule {

    companion object {
        const val QUALIFIER_MEDIA = "QUALIFIER_MEDIA"
    }

    @Provides
    @Named(QUALIFIER_MEDIA)
    fun provideMedia(mediaDetailFragment: MediaDetailFragment) = mediaDetailFragment.args.media
}

@Module
class SearchFragmentModule {

    companion object {
        const val QUALIFIER_SCREEN_TYPE = "QUALIFIER_SCREEN_TYPE"
        const val QUALIFIER_INITIAL_DATA = "QUALIFIER_INITIAL_DATA"
    }

    @Provides
    @Named(QUALIFIER_SCREEN_TYPE)
    fun provideScreenType(searchFragment: SearchFragment) = searchFragment.args.category.screenType
    @Provides
    @Named(QUALIFIER_INITIAL_DATA)
    fun provideInitialData(searchFragment: SearchFragment) = searchFragment.args.category.initialMediaItems
}