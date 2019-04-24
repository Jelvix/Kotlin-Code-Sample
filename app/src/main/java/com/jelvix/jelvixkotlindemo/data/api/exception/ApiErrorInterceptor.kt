/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api.exception

import android.content.Context
import com.google.gson.Gson
import com.jelvix.jelvixkotlindemo.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.nio.charset.Charset

class ApiErrorInterceptor(
        private val applicationContext: Context,
        private val gson: Gson
): Interceptor {

    companion object {
        private val UTF8 = Charset.forName("UTF-8")
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val originalRequest = chain.request()
            val response = chain.proceed(originalRequest)
            return if (response.code() >= 400) {
                throwError(response, response.code())
                response
            } else {
                response
            }
        } catch (e: Exception) {
            when(e) {
                is ConnectException -> {
                    throw ApiException.generateInternalServerError(ApiException.CODE_INTERNAL_SERVER, applicationContext)
                }
                is SocketTimeoutException -> {
                    throw ApiException.generateNoInternetError(applicationContext)
                }
                else -> throw e
            }
        }

    }

    @Throws(IOException::class, ApiException::class)
    private fun throwError(response: Response, code: Int) {
        if (code >= ApiException.CODE_INTERNAL_SERVER) {
            throw ApiException.generateInternalServerError(code, applicationContext)
        }
        val responseBody = response.body()
        val source = responseBody?.source()
        source?.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source?.buffer()

        var charset: Charset = UTF8
        val contentType = responseBody?.contentType()
        contentType?.charset(UTF8)?.let {
            charset = it
        }

        if (responseBody?.contentLength() != 0L) {
            val responseJSON = buffer?.clone()?.readString(charset)

            val errorDetails = try {
                gson.fromJson(responseJSON, ErrorDetails::class.java)
            } catch (e: Exception) {
                ErrorDetails.generateUnknownError(applicationContext.getString(R.string.unknown_error))
            }
            throw ApiException(code, errorDetails)
        } else {
            throw ApiException(code, ErrorDetails())
        }
    }
}