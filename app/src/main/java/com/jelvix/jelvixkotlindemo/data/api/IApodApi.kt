/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api

import com.jelvix.jelvixkotlindemo.data.api.entity.Apod
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface IApodApi {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("date") date: String?, @Query("hd") isWithHighResolution: Boolean): Single<Apod>
}