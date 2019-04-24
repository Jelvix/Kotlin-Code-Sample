/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.manager

import com.jelvix.jelvixkotlindemo.data.api.entity.Apod
import com.jelvix.jelvixkotlindemo.data.api.entity.Link
import com.jelvix.jelvixkotlindemo.data.api.entity.MediaApiItem
import com.jelvix.jelvixkotlindemo.data.api.response.ResponseCollection
import io.reactivex.Single

interface IApiManager {

    fun searchMedia(page: Int?, query: String?, mediaType: String?): Single<ResponseCollection<MediaApiItem>>

    fun getMediaAssetsManifest(nasaId: String?): Single<ResponseCollection<Link>>

    fun getPictureOfTheDay(date: String): Single<Apod>
}