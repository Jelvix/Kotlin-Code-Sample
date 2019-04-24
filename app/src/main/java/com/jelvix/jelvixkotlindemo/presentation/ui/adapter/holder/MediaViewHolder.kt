/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.adapter.holder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.MediaRecyclerViewAdapter
import com.jelvix.jelvixkotlindemo.presentation.util.DateTimeUtils
import com.jelvix.jelvixkotlindemo.presentation.util.ext.loadCenterCrop
import kotlinx.android.synthetic.main.item_media.view.*

class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val VIEW_RES = R.layout.item_media
    }

    fun bind(
        item: MediaItem?,
        mediaRecyclerClickListener: MediaRecyclerViewAdapter.IMediaRecyclerClickListener
    ) = with(itemView) {
        item ?: return@with
        imageView.loadCenterCrop(item.previewLink)
        titleTextView.text = item.title
        descriptionTextView.text = item.description
        when (item.mediaType) {
            MediaType.IMAGE -> {
                mediaTypeImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_image
                    )
                )
            }
            MediaType.VIDEO -> {
                mediaTypeImageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_video
                    )
                )
            }
            null -> {
                mediaTypeImageView.setImageDrawable(null)
            }
        }
        dateCreationTextView.text = DateTimeUtils.formatRelativeDays(item.dateCreated)
        setOnClickListener {
            mediaRecyclerClickListener.onItemClick(item, imageView)
        }
    }
}