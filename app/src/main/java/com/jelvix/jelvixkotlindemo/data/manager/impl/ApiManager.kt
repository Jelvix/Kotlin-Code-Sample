/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.manager.impl

import com.jelvix.jelvixkotlindemo.data.api.IApodApi
import com.jelvix.jelvixkotlindemo.data.api.INasaApi
import com.jelvix.jelvixkotlindemo.data.api.entity.Apod
import com.jelvix.jelvixkotlindemo.data.api.entity.Link
import com.jelvix.jelvixkotlindemo.data.api.entity.MediaApiItem
import com.jelvix.jelvixkotlindemo.data.api.response.ResponseCollection
import com.jelvix.jelvixkotlindemo.data.manager.IApiManager
import io.reactivex.Single

class ApiManager(
    private val nasaApi: INasaApi,
    private val apodApi: IApodApi
) : IApiManager {

    override fun searchMedia(page: Int?, query: String?, mediaType: String?): Single<ResponseCollection<MediaApiItem>> {
        return nasaApi.searchMedia(page, query, mediaType)
    }

    override fun getMediaAssetsManifest(nasaId: String?): Single<ResponseCollection<Link>> {
        return nasaApi.getMediaAssetsManifest(nasaId)
    }

    override fun getPictureOfTheDay(date: String): Single<Apod> {
        return apodApi.getPictureOfTheDay(date, true)
    }
}