/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.domain

import com.jelvix.jelvixkotlindemo.data.api.ApiConstants
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.manager.impl.DbManager
import com.jelvix.jelvixkotlindemo.presentation.util.bus.MediaItemAddedToFavorites
import com.jelvix.jelvixkotlindemo.presentation.util.bus.MediaItemRemovedFromFavorites
import com.jelvix.jelvixkotlindemo.presentation.util.bus.RxBus
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface IDbInteractor {
    fun insertMediaItem(mediaItem: MediaItem): Completable
    fun removeMediaItem(mediaItem: MediaItem): Completable
    fun isMediaItemInFavorites(mediaItem: MediaItem): Single<Boolean>
    fun getMediaItems(query: String?, page: Int?): Single<MutableList<MediaItem>>
    fun getMediaItemsWithTotalCount(query: String?, page: Int?): Single<Pair<MutableList<MediaItem>, Int>>
}

class DbInteractor @Inject constructor(
    private val dbManager: DbManager
) : IDbInteractor {

    override fun insertMediaItem(mediaItem: MediaItem): Completable {
        mediaItem.isFavorite = true
        return dbManager.insertMediaItem(mediaItem)
            .doOnComplete {
                RxBus.sendEvent(MediaItemAddedToFavorites(mediaItem))
            }
    }

    override fun removeMediaItem(mediaItem: MediaItem): Completable {
        return dbManager.removeMediaItem(mediaItem)
            .doOnComplete {
                RxBus.sendEvent(MediaItemRemovedFromFavorites(mediaItem))
            }
    }

    override fun isMediaItemInFavorites(mediaItem: MediaItem): Single<Boolean> {
        val nasaId = mediaItem.nasaId ?: return Single.just(false)
        return dbManager.getFavoriteMediaItemByNasaId(nasaId).isEmpty.map { !it }
    }

    override fun getMediaItems(query: String?, page: Int?): Single<MutableList<MediaItem>> {
        return dbManager.getMediaItems(query, ((page ?: 0) - 1) * ApiConstants.DEFAULT_LIMIT, ApiConstants.DEFAULT_LIMIT)
    }

    override fun getMediaItemsWithTotalCount(query: String?, page: Int?): Single<Pair<MutableList<MediaItem>, Int>> {
        return getMediaItems(query, page)
            .flatMap {items ->
                dbManager.getItemsTotalCount().map { totalCounts ->
                    items to totalCounts
                }
            }
    }
}