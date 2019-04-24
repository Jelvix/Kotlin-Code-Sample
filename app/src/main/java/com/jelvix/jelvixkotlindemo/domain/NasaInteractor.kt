/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.domain

import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.entity.OriginMedia
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.data.manager.IApiManager
import io.reactivex.Single
import javax.inject.Inject

interface INasaInteractor {
    fun searchMedia(page: Int?, query: String?, mediaType: MediaType): Single<List<MediaItem>>
    fun searchMediaWithTotalCount(page: Int?, query: String?, mediaType: MediaType): Single<Pair<List<MediaItem>, Int>>
    fun getOriginMedia(nasaId: String?): Single<OriginMedia>
}

class NasaInteractor @Inject constructor(
    private val apiManager: IApiManager
) : INasaInteractor {

    override fun searchMedia(page: Int?, query: String?, mediaType: MediaType): Single<List<MediaItem>> {
        return searchMediaWithTotalCount(page, query, mediaType).map { (medias, _) -> medias }
    }

    override fun searchMediaWithTotalCount(page: Int?, query: String?, mediaType: MediaType): Single<Pair<List<MediaItem>, Int>> {
        return apiManager.searchMedia(page, query, mediaType.name.toLowerCase()).map { response ->
            (response.collection?.items?.map { MediaItem.mapFromMediaApiItem(it)} ?: listOf()) to (response.collection?.metadata?.totalHits ?: 0)
        }
    }

    override fun getOriginMedia(nasaId: String?): Single<OriginMedia> {
        return apiManager.getMediaAssetsManifest(nasaId).map {
            OriginMedia.createOriginMediaFromLinkList(it.collection?.items)
        }
    }
}