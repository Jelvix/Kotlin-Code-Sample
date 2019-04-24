/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.exception

import android.content.Context
import com.jelvix.jelvixkotlindemo.R

class ApiException(
        val errorCode: Int,
        val errorDetails: ErrorDetails
) : Exception() {

    companion object {
        const val CODE_NO_INTERNET = -1
        const val CODE_INTERNAL_SERVER = 500

        fun generateInternalServerError(code: Int, applicationContext: Context): ApiException {
            return ApiException(code, ErrorDetails.generateInternalServerErrorDetail(applicationContext.getString(R.string.internal_server_error_message)))
        }
        fun generateNoInternetError(applicationContext: Context): ApiException {
            return ApiException(CODE_NO_INTERNET, ErrorDetails.generateNoInternetErrorDetail(applicationContext.getString(R.string.network_error_message)))
        }
    }

    override var message: String? = null

    init {
        this.message = errorDetails.body
    }
}
