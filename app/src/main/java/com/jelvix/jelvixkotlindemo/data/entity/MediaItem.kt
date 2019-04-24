/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.jelvix.jelvixkotlindemo.data.api.entity.Apod
import com.jelvix.jelvixkotlindemo.data.api.entity.MediaApiItem
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.data.enums.Relation
import java.util.*

@Entity(tableName = "media_item")
class MediaItem() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null
    @ColumnInfo(name = "nasa_id")
    var nasaId: String? = null
    @ColumnInfo(name = "title")
    var title: String? = null
    @ColumnInfo(name = "description")
    var description: String? = null
    var mediaType: MediaType? = null
    var dateCreated: Date? = null
    var center: String? = null
    var photographer: String? = null
    var keywords: List<String>? = null
    var previewLink: String? = null
    @Embedded(prefix = "origin_media_")
    var originMedia: OriginMedia? = null
    var isFavorite: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        return other is MediaItem && other.nasaId == this.nasaId
    }

    override fun hashCode(): Int = nasaId?.hashCode() ?: 0

    @Ignore
    constructor(source: Parcel) : this() {
        nasaId = source.readString()
        title = source.readString()
        description = source.readString()
        mediaType = source.readValue(Int::class.java.classLoader)?.let { MediaType.values()[it as Int] }
        dateCreated = source.readSerializable() as Date?
        center = source.readString()
        photographer = source.readString()
        keywords = source.createStringArrayList()
        previewLink = source.readString()
        originMedia =  source.readParcelable(OriginMedia::class.java.classLoader)
        isFavorite = 1 == source.readInt()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(nasaId)
        writeString(title)
        writeString(description)
        writeValue(mediaType?.ordinal)
        writeSerializable(dateCreated)
        writeString(center)
        writeString(photographer)
        writeStringList(keywords)
        writeString(previewLink)
        writeParcelable(originMedia, 0)
        writeInt((if (isFavorite) 1 else 0))
    }

    companion object {

        fun mapFromMediaApiItem(mediaApiItem: MediaApiItem?): MediaItem {
            val media = mediaApiItem?.data?.firstOrNull()
            return MediaItem().apply {
                this.nasaId = media?.nasaId
                this.title = media?.title
                this.description = media?.description
                this.mediaType = media?.mediaType
                this.dateCreated = media?.dateCreated
                this.center = media?.center
                this.photographer = media?.photographer
                this.keywords = media?.keywords
                this.previewLink = mediaApiItem?.links?.firstOrNull { it.rel == Relation.PREVIEW }?.href
                this.originMedia = null
            }
        }

        fun mapFromApod(apod: Apod?): MediaItem {
            return MediaItem().apply {
                this.nasaId = apod?.dateCreated
                this.title = apod?.title
                this.description = apod?.explanation
                this.mediaType = apod?.mediaType
                this.dateCreated = Apod.apodDateFormat.parse(apod?.dateCreated)
                this.previewLink = apod?.url
                this.originMedia = OriginMedia().apply {
                    this.mediaType = apod?.mediaType
                    this.originUrl = apod?.hdurl ?: apod?.url
                }
            }
        }

        @JvmField
        val CREATOR: Parcelable.Creator<MediaItem> = object : Parcelable.Creator<MediaItem> {
            override fun createFromParcel(source: Parcel): MediaItem = MediaItem(source)
            override fun newArray(size: Int): Array<MediaItem?> = arrayOfNulls(size)
        }
    }
}

@Fts4(contentEntity = MediaItem::class)
@Entity(tableName = "media_item_fts")
class MediaItemFts {
    @ColumnInfo(name = "title")
    var title: String? = null
    @ColumnInfo(name = "description")
    var description: String? = null
}