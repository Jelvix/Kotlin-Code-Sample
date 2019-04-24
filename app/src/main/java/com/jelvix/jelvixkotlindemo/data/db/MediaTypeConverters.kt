/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import java.util.*

class MediaTypeConverters {

    @TypeConverter
    fun stringToMutableStringList(data: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(data, listType)
    }

    @TypeConverter
    fun mutableStringListToString(carouselMediaList: List<String>?): String {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(carouselMediaList, listType)
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        if (date == null) {
            return null
        } else {
            return date.time
        }
    }

    @TypeConverter
    fun mediaTypeToString(value: MediaType?): String? {
        return value?.name
    }

    @TypeConverter
    fun stringToMediaType(value: String?): MediaType? {
        return if (value != null) {
            MediaType.valueOf(value)
        } else {
            null
        }
    }
}