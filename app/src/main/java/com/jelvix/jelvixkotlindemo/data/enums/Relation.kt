/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.enums

import com.google.gson.annotations.SerializedName

enum class Relation {
    @SerializedName("next")
    NEXT,
    @SerializedName("prev")
    PREV,
    @SerializedName("preview")
    PREVIEW,
    @SerializedName("caption")
    CAPTIONS
}