/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.data.entity

import android.os.Parcel
import android.os.Parcelable
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType

class Category(
    val screenType: ScreenType,
    var initialMediaItems: List<MediaItem>? = null,
    var lastShowedMediaItem: MediaItem? = null,
    var totalCount: Int? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        ScreenType.values()[source.readInt()],
        source.createTypedArrayList(MediaItem.CREATOR),
        source.readParcelable<MediaItem>(MediaItem::class.java.classLoader),
        source.readValue(Int::class.java.classLoader) as Int?
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(screenType.ordinal)
        writeTypedList(initialMediaItems)
        writeParcelable(lastShowedMediaItem, 0)
        writeValue(totalCount)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}