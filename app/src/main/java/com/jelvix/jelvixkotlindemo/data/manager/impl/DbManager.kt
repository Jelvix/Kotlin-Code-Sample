/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.manager.impl

import com.jelvix.jelvixkotlindemo.data.db.MediaItemDao
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.manager.IDbManager
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DbManager @Inject constructor(
    private val mediaItemDao: MediaItemDao
) : IDbManager {

    override fun insertMediaItem(mediaItem: MediaItem): Completable {
        return mediaItemDao.insertMediaItem(mediaItem).subscribeOn(Schedulers.io())
    }

    override fun removeMediaItem(mediaItem: MediaItem): Completable {
        return mediaItemDao.deleteMediaItem(mediaItem.nasaId).subscribeOn(Schedulers.io())
    }

    override fun getFavoriteMediaItemByNasaId(nasaId: String): Maybe<MediaItem?> {
        return mediaItemDao.getFavoriteMediaItemByNasaId(nasaId).subscribeOn(Schedulers.io())
    }

    override fun getMediaItemByNasaId(nasaId: String): Maybe<MediaItem?> {
        return mediaItemDao.getMediaItemByNasaId(nasaId).subscribeOn(Schedulers.io())
    }

    override fun getMediaItems(query: String?, offset: Int, limit: Int): Single<MutableList<MediaItem>> {
        return if (query.isNullOrEmpty()) {
            mediaItemDao.getMediaItems(offset, limit).subscribeOn(Schedulers.io())
        } else {
            mediaItemDao.getMediaItemsWithQuery(String.format("*%s*", query), offset, limit).subscribeOn(Schedulers.io())
        }
    }

    override fun getItemsTotalCount(): Single<Int> {
        return mediaItemDao.getItemsTotalCount()
    }
}