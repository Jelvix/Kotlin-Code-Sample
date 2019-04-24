/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.presentation.util.ext.mergeNewItemList

abstract class AutoLoadingRecyclerViewAdapter<Holder : RecyclerView.ViewHolder, Item : Any?>(
    val itemList: MutableList<Item> = mutableListOf(),
    protected val emptyItem: EmptyItem? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val VIEW_TYPE_PROGRESSBAR = 0
    protected val VIEW_TYPE_ITEM = 1
    protected val VIEW_EMPTY = 2

    var isFooterEnabled = true

    open fun mergeNewItemList(items: List<Item>) {
        itemList.mergeNewItemList(items)
        notifyDataSetChanged()
    }

    open fun removeItem(item: Item) {
        val position = itemList.indexOf(item)
        itemList.remove(item)
        if (position != -1) {
            notifyItemRemoved(position)
        }
    }

    open fun clearItems() {
        itemList.clear()
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = if (position >= itemList.size) VIEW_TYPE_PROGRESSBAR else VIEW_TYPE_ITEM
        if (viewType == VIEW_TYPE_PROGRESSBAR) {
            viewType = if (isFooterEnabled) VIEW_TYPE_PROGRESSBAR else VIEW_EMPTY
        }
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        VIEW_TYPE_PROGRESSBAR -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false)
            val viewHolder = ProgressViewHolder(view)
            val layoutParams = viewHolder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
            viewHolder
        }
        VIEW_EMPTY -> {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empty, parent, false)
            EmptyViewHolder(view)
        }
        else -> onNestedCreateViewHolder(parent, viewType)
    }


    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProgressViewHolder -> holder.progressBar.isIndeterminate = true
            is EmptyViewHolder -> holder.bindEmptyItem(emptyItem)
            else -> onNestedBindViewHolder(holder as Holder, position)
        }
    }

    abstract fun onNestedCreateViewHolder(parent: ViewGroup, viewType: Int): Holder

    abstract fun onNestedBindViewHolder(holder: Holder, position: Int)

    open fun enableFooter(isEnabled: Boolean) {
        if (this.isFooterEnabled == isEnabled) {
            return
        }
        this.isFooterEnabled = isEnabled
        if (isEnabled) {
            notifyItemInserted(itemCount)
        } else {
            notifyItemRemoved(itemCount + 1)
        }
    }

    override fun getItemCount(): Int {
        val size = itemList.size
        val isEmptyItem = ((size == 0) && (emptyItem != null))
        return if (isFooterEnabled || isEmptyItem) size + 1 else size
    }


    /* Inner Classes */

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val progressBar: ProgressBar = v.findViewById(R.id.progressBar)
    }

    class EmptyViewHolder(item: View) : RecyclerView.ViewHolder(item) {

        private val textView: TextView = item.findViewById(R.id.textView)

        fun bindEmptyItem(emptyItem: EmptyItem?) {
            if (emptyItem?.stringRes != null) {
                textView.setText(emptyItem.stringRes)
            } else if (emptyItem?.text != null) {
                textView.text = emptyItem.text
            }
        }
    }

    class EmptyItem(
        @StringRes
        val stringRes: Int? = null,
        val text: String? = null
    )
}
