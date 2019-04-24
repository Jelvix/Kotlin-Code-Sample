/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.entity

import com.google.gson.annotations.SerializedName
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.data.enums.Relation

class Link(
    @SerializedName("href")
    val href: String?,
    @SerializedName("prompt")
    val prompt: String?,
    @SerializedName("render")
    val render: MediaType?,
    @SerializedName("rel")
    val rel: Relation?
)