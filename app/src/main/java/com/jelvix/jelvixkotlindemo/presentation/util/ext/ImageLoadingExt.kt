/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.util.ext

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.Target
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.presentation.inject.glide.GlideApp
import com.jelvix.jelvixkotlindemo.presentation.inject.glide.GlideRequest
import jp.wasabeef.glide.transformations.BlurTransformation

const val CROSS_FADE_DURATION = 300

object LottieHelper {
    var cashedComposition: HashMap<Int, LottieComposition> = HashMap()
}

fun ImageView.loadApodCategoryImage(path: String?) {
    GlideApp.with(this)
        .load(path)
        .centerCrop()
        .transition(DrawableTransitionOptions().crossFade(CROSS_FADE_DURATION))
        .into(this)
}

fun ImageView.loadBlurredCategoryImage(path: String?) {
    val transformation = MultiTransformation<Bitmap>(
        CenterCrop(),
        BlurTransformation(10)
    )
    val previousDrawable = drawable
    GlideApp.with(this)
        .load(path)
        .placeholder(previousDrawable)
        .apply(bitmapTransform(transformation))
        .transition(DrawableTransitionOptions().crossFade(CROSS_FADE_DURATION))
        .into(this)
}

fun ImageView.loadCenterCrop(path: String?) {
    GlideApp.with(this)
        .load(path)
        .lottiePlaceholder(context, R.raw.planet_loading)
        .error(R.drawable.ic_warning)
        .dontTransform()
        .transition(DrawableTransitionOptions().crossFade(CROSS_FADE_DURATION))
        .centerCrop()
        .into(this)
}

fun ImageView.loadDetailedMediaPreview(path: String?) {
    GlideApp.with(this)
        .load(path)
        .fitCenter()
        .apply(bitmapTransform(BlurTransformation(25, 3)))
        .transition(DrawableTransitionOptions().crossFade(CROSS_FADE_DURATION))
        .into(this)
}

fun ImageView.loadDetailedMediaImage(path: String?, onLoadFinished: () -> Unit) {
    GlideApp.with(this)
        .load(path)
        .fitCenter()
        .addSimpleListener {
            onLoadFinished()
        }
        .into(this)
}

private fun <TranscodeType> GlideRequest<TranscodeType>.addSimpleListener(listener: (isSuccess: Boolean) -> Unit): GlideRequest<TranscodeType> {
    return this
        .addListener(object: RequestListener<TranscodeType> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<TranscodeType>?,
                isFirstResource: Boolean
            ): Boolean {
                listener(false)
                return false
            }

            override fun onResourceReady(
                resource: TranscodeType,
                model: Any?,
                target: Target<TranscodeType>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                listener(true)
                return false
            }
        })
}

private fun <TranscodeType> GlideRequest<TranscodeType>.lottiePlaceholder(context: Context, @RawRes resId: Int): GlideRequest<TranscodeType> {
    val placeholder = LottieDrawable()
    if (LottieHelper.cashedComposition.containsKey(resId)) {
        placeholder.composition = LottieHelper.cashedComposition[resId]
        placeholder.repeatCount = 0
        placeholder.playAnimation()
    } else {
        val task = LottieCompositionFactory.fromRawRes(context, resId)
        val lottieListener: (LottieComposition) -> Unit = { result ->
            LottieHelper.cashedComposition[resId] = result
            placeholder.composition = result
            placeholder.repeatCount = 0
            placeholder.playAnimation()
        }
        task.addListener(lottieListener)
    }
    return this
        .placeholder(placeholder)
        .addSimpleListener {
            placeholder.stop()
            placeholder.clearComposition()
        }
}