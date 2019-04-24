/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.entity.MediaItemFts

@Database(entities = [MediaItem::class, MediaItemFts::class], version = 1, exportSchema = false)
@TypeConverters(MediaTypeConverters::class)
abstract class AppDb : RoomDatabase() {

    abstract fun mediaItemDao(): MediaItemDao
}