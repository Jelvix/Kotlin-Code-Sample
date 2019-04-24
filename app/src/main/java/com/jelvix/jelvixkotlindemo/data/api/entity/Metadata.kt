/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.entity

import com.google.gson.annotations.SerializedName

class Metadata(
    @SerializedName("total_hits")
    val totalHits: Int?
)