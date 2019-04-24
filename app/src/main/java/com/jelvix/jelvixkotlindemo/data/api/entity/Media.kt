/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.entity

import com.google.gson.annotations.SerializedName
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import java.util.*

class Media(
    @SerializedName("nasa_id")
    val nasaId: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("media_type")
    val mediaType: MediaType?,
    @SerializedName("date_created")
    val dateCreated: Date?,
    @SerializedName("center")
    val center: String?,
    @SerializedName("photographer")
    val photographer: String?,
    @SerializedName("keywords")
    val keywords: List<String>?
)