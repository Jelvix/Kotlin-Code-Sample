/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.enums

import com.google.gson.annotations.SerializedName

enum class MediaType {
    @SerializedName("image")
    IMAGE,
    @SerializedName("video")
    VIDEO
}