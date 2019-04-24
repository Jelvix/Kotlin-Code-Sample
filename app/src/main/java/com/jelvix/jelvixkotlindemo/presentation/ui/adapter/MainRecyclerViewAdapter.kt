/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jelvix.jelvixkotlindemo.data.entity.Category
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.holder.CategoryViewHolder

class MainRecyclerViewAdapter(
    val items: MutableList<Category>,
    val mainRecyclerClickListener: IMainRecyclerClickListener
) : RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(CategoryViewHolder.VIEW_RES, parent, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.forEach {
                when (it) {
                    PAYLOAD_MEDIA_ITEMS_LOADED -> {
                        holder.changeCategoryImage(items.getOrNull(position))
                    }
                    PAYLOAD_TOTAL_COUNT -> {
                        holder.changeTotalCount(items.getOrNull(position))
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items.getOrNull(position), mainRecyclerClickListener)
    }

    fun onMediaTick(mediaItem: MediaItem, screenType: ScreenType) {
        val category = items.find { it.screenType == screenType } ?: return
        category.lastShowedMediaItem = mediaItem
        val position = items.indexOf(category)
        notifyItemChanged(position, PAYLOAD_MEDIA_ITEMS_LOADED)
    }

    fun onTotalCountLoaded(totalCount: Int, screenType: ScreenType) {
        val category = items.find { it.screenType == screenType } ?: return
        category.totalCount = totalCount
        val position = items.indexOf(category)
        notifyItemChanged(position, PAYLOAD_TOTAL_COUNT)
    }

    interface IMainRecyclerClickListener {
        fun onItemClick(category: Category?)
    }

    companion object {
        const val PAYLOAD_TOTAL_COUNT = "PAYLOAD_TOTAL_COUNT"
        const val PAYLOAD_MEDIA_ITEMS_LOADED = "PAYLOAD_MEDIA_ITEMS_LOADED"
    }
}