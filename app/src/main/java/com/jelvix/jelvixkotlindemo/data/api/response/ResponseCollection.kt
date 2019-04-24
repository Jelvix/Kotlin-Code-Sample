/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.response

import com.google.gson.annotations.SerializedName
import com.jelvix.jelvixkotlindemo.data.api.entity.Link
import com.jelvix.jelvixkotlindemo.data.api.entity.Metadata

class ResponseCollection<T>(
    @SerializedName("collection")
    val collection: Collection<T>?
)

class Collection<T>(
    @SerializedName("href")
    val href: String?,
    @SerializedName("metadata")
    val metadata: Metadata?,
    @SerializedName("links")
    val links: List<Link>?,
    @SerializedName("items")
    val items: List<T>?
)