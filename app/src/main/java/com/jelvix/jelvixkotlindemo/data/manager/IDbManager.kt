/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.manager

import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface IDbManager {

    fun insertMediaItem(mediaItem: MediaItem): Completable

    fun removeMediaItem(mediaItem: MediaItem): Completable

    fun getFavoriteMediaItemByNasaId(nasaId: String): Maybe<MediaItem?>

    fun getMediaItemByNasaId(nasaId: String): Maybe<MediaItem?>

    fun getMediaItems(query: String?, offset: Int, limit: Int): Single<MutableList<MediaItem>>

    fun getItemsTotalCount(): Single<Int>
}