/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.api

import com.jelvix.jelvixkotlindemo.data.api.entity.Link
import com.jelvix.jelvixkotlindemo.data.api.entity.MediaApiItem
import com.jelvix.jelvixkotlindemo.data.api.response.ResponseCollection
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface INasaApi {

    @GET("search")
    fun searchMedia(
        @Query("page") page: Int?,
        @Query("q") query: String?,
        @Query("media_type") mediaType: String?
    ): Single<ResponseCollection<MediaApiItem>>

    @GET("asset/{nasaId}")
    fun getMediaAssetsManifest(
        @Path("nasaId") nasaId: String?
    ): Single<ResponseCollection<Link>>
}