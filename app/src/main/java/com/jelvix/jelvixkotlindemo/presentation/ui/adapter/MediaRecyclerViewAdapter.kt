/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.holder.MediaViewHolder
import com.jelvix.jelvixkotlindemo.presentation.util.pagination.AutoLoadingRecyclerViewAdapter

class MediaRecyclerViewAdapter(
    val items: MutableList<MediaItem>,
    val mediaRecyclerClickListener: IMediaRecyclerClickListener
) : AutoLoadingRecyclerViewAdapter<RecyclerView.ViewHolder, MediaItem>(items, EmptyItem(R.string.no_items_found)) {

    override fun onNestedCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MediaViewHolder(LayoutInflater.from(parent.context).inflate(MediaViewHolder.VIEW_RES, parent, false))
    }

    override fun onNestedBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is MediaViewHolder -> holder.bind(items.getOrNull(position), mediaRecyclerClickListener)
        }
    }

    interface IMediaRecyclerClickListener {
        fun onItemClick(media: MediaItem, view: View)
    }
}