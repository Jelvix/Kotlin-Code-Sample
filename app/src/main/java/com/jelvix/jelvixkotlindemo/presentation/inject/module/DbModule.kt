/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject.module

import android.app.Application
import androidx.room.Room
import com.jelvix.jelvixkotlindemo.data.db.AppDb
import com.jelvix.jelvixkotlindemo.data.db.MediaItemDao
import com.jelvix.jelvixkotlindemo.data.manager.IDbManager
import com.jelvix.jelvixkotlindemo.data.manager.impl.DbManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb = Room.databaseBuilder(app, AppDb::class.java, "appDb.db").build()

    @Singleton
    @Provides
    fun provideProductDao(db: AppDb): MediaItemDao = db.mediaItemDao()

    @Provides
    fun provideIDbManager(dbManager: DbManager): IDbManager = dbManager
}