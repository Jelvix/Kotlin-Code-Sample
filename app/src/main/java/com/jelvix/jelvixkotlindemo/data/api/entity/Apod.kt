/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.entity

import com.google.gson.annotations.SerializedName
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import org.apache.commons.lang3.time.FastDateFormat
import java.util.*

class Apod(
    @SerializedName("copyright")
    val copyright: String?,
    @SerializedName("date")
    val dateCreated: String?,
    @SerializedName("explanation")
    val explanation: String?,
    @SerializedName("hdurl")
    val hdurl: String?,
    @SerializedName("media_type")
    val mediaType: MediaType?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("url")
    val url: String?
) {
    companion object {
        val apodDateFormat = FastDateFormat.getInstance("yyyy-MM-dd", Locale.US)
    }
}