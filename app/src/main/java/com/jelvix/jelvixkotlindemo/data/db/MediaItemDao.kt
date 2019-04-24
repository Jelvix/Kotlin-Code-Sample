/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface MediaItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMediaItem(mediaItem: MediaItem): Completable

    @Query("DELETE FROM media_item WHERE media_item.nasa_id = :nasaId")
    fun deleteMediaItem(nasaId: String?): Completable

    @Query("SELECT * FROM media_item WHERE media_item.nasa_id = :nasaId AND media_item.isFavorite = 1")
    fun getFavoriteMediaItemByNasaId(nasaId: String): Maybe<MediaItem?>

    @Query("SELECT * FROM media_item WHERE media_item.nasa_id = :nasaId")
    fun getMediaItemByNasaId(nasaId: String): Maybe<MediaItem?>

    @Query("SELECT COUNT(media_item.nasa_id) FROM media_item WHERE media_item.isFavorite = 1")
    fun getItemsTotalCount(): Single<Int>

    @Query("""SELECT * FROM media_item
        JOIN media_item_fts ON (media_item.id = media_item_fts.docid) WHERE media_item_fts MATCH :query AND media_item.isFavorite = 1
        LIMIT :limit OFFSET :offset""")
    fun getMediaItemsWithQuery(query: String?, offset: Int, limit: Int): Single<MutableList<MediaItem>>

    @Query("""SELECT * FROM media_item WHERE media_item.isFavorite = 1
        LIMIT :limit OFFSET :offset""")
    fun getMediaItems(offset: Int, limit: Int): Single<MutableList<MediaItem>>
}