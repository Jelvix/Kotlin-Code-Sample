/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Ignore
import com.jelvix.jelvixkotlindemo.data.api.entity.Link
import com.jelvix.jelvixkotlindemo.data.enums.MediaType

class OriginMedia() : Parcelable {

    var originUrl: String? = null
    var mediaType: MediaType? = null
    var subtitleUrl: String? = null

    @Ignore
    constructor(source: Parcel) : this() {
        originUrl = source.readString()
        mediaType = source.readValue(Int::class.java.classLoader)?.let { MediaType.values()[it as Int] }
        subtitleUrl = source.readString()
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(originUrl)
        writeValue(mediaType?.ordinal)
        writeString(subtitleUrl)
    }

    companion object {

        private val supportedImageFormats = listOf("jpg", "jpeg", "png", "gif", "bmp")
        private val supportedVideoFormats = listOf("mp4")
        private val supportedFormats = supportedImageFormats + supportedVideoFormats
        private val originRegex = Regex(
            "^.*~orig(${supportedFormats.joinToString(
                separator = "|",
                transform = { ".$it" }
            )})$"
        )
        private val largeRegex = Regex(
            "^.*~large(${supportedFormats.joinToString(
                separator = "|",
                transform = { ".$it" }
            )})$"
        )
        private val supportedFormatsRegex = Regex(
            "^.*(${supportedFormats.joinToString(
                separator = "|",
                transform = { ".$it" }
            )})$"
        )

        fun createOriginMediaFromLinkList(links: List<Link>?): OriginMedia {
            links ?: return OriginMedia()
            val originUrl = links.firstOrNull { link ->
                link.href?.contains(originRegex) == true
            }?.href ?: links.firstOrNull { link ->
                link.href?.contains(largeRegex) == true
            }?.href ?: links.firstOrNull { link ->
                link.href?.contains(supportedFormatsRegex) == true
            }?.href ?: ""
            val extension = originUrl.substringAfterLast('.')
            val mediaType = when (extension) {
                in supportedImageFormats -> MediaType.IMAGE
                in supportedVideoFormats -> MediaType.VIDEO
                else -> null
            }
            var subtitleUrl: String? = null
            if (mediaType == MediaType.VIDEO) {
                subtitleUrl = links.firstOrNull { link ->
                    link.href?.endsWith(".srt") == true
                }?.href ?: ""
            }
            return OriginMedia().apply {
                this.originUrl = originUrl
                this.mediaType = mediaType
                this.subtitleUrl = subtitleUrl
            }
        }

        @JvmField
        val CREATOR: Parcelable.Creator<OriginMedia> = object : Parcelable.Creator<OriginMedia> {
            override fun createFromParcel(source: Parcel): OriginMedia = OriginMedia(source)
            override fun newArray(size: Int): Array<OriginMedia?> = arrayOfNulls(size)
        }
    }
}