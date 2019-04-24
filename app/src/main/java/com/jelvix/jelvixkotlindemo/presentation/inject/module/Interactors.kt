/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject.module

import com.jelvix.jelvixkotlindemo.domain.*
import dagger.Module
import dagger.Provides

@Module
class NasaInteractorModule {
    @Provides
    fun provideNasaInteractor(nasaInteractor: NasaInteractor): INasaInteractor = nasaInteractor
}

@Module
class DbInteractorModule {
    @Provides
    fun provideDbInteractor(dbInteractor: DbInteractor): IDbInteractor = dbInteractor
}

@Module
class ApodInteractorModule {
    @Provides
    fun provideApodInteractor(apodInteractor: ApodInteractor): IApodInteractor = apodInteractor
}