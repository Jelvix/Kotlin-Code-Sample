/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.exception

import com.google.gson.annotations.SerializedName

class ErrorDetails(
        @SerializedName("subject")
        var subject: String? = null,
        @SerializedName("reason")
        var body: String? = null
) {

    companion object {
        fun generateUnknownError(message: String) = ErrorDetails("", message)
        fun generateInternalServerErrorDetail(message: String) = ErrorDetails("", message)
        fun generateNoInternetErrorDetail(message: String) = ErrorDetails("", message)
    }
}