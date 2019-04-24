/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.entity

import com.google.gson.annotations.SerializedName

class MediaApiItem(
    @SerializedName("href")
    val href: String?,
    @SerializedName("links")
    val links: List<Link>?,
    @SerializedName("data")
    val data: List<Media>?
)