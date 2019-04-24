/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.adapter.holder

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.Category
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.MainRecyclerViewAdapter
import com.jelvix.jelvixkotlindemo.presentation.util.ext.loadApodCategoryImage
import com.jelvix.jelvixkotlindemo.presentation.util.ext.loadBlurredCategoryImage
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        const val VIEW_RES = R.layout.item_category
    }

    fun bind(
        category: Category?,
        mainRecyclerClickListener: MainRecyclerViewAdapter.IMainRecyclerClickListener
    ) = with(itemView) {
        (imageView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio =
            when (category?.screenType) {
                ScreenType.APOD -> "1:1"
                else -> "16:6"
            }
        changeTotalCount(category)
        changeCategoryImage(category)
        setOnClickListener {
            mainRecyclerClickListener.onItemClick(category)
        }
    }

    fun changeCategoryImage(category: Category?) = with(itemView) {
        when (category?.screenType) {
            ScreenType.APOD -> imageView.loadApodCategoryImage(category.lastShowedMediaItem?.previewLink)
            else -> imageView.loadBlurredCategoryImage(category?.lastShowedMediaItem?.previewLink)
        }
        titleTextView.text = category?.lastShowedMediaItem?.title
    }

    fun changeTotalCount(category: Category?) = with(itemView) {
        val categoryNameStringBuilder = SpannableStringBuilder()
        categoryNameStringBuilder.append(
            when (category?.screenType) {
                ScreenType.APOD -> {
                    "Picture of The Day"
                }
                ScreenType.IMAGE -> {
                    context.getString(R.string.images)
                }
                ScreenType.VIDEO -> {
                    context.getString(R.string.videos)
                }
                ScreenType.FAVORITE -> {
                    context.getString(R.string.favorites)
                }
                null -> {
                    ""
                }
            }
        )
        category?.totalCount?.let {
            val hitsCount = "  ($it)"
            categoryNameStringBuilder.append(hitsCount)
            categoryNameStringBuilder.setSpan(
                RelativeSizeSpan(0.5f),
                categoryNameStringBuilder.indexOf(hitsCount),
                categoryNameStringBuilder.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        categoreNameTextView.text = categoryNameStringBuilder
    }
}