/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.inject.glide

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import io.reactivex.Single
import java.io.File
import java.util.*

@GlideModule
open class JelvixDemoKotlinAppGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        clearGlideCache(context)
    }

    private fun clearGlideCache(context: Context) {
        val expirationCacheCalendar = Calendar.getInstance()
        expirationCacheCalendar.add(Calendar.DAY_OF_MONTH, -14)
        val removeOldCacheDisposable = Single.just(expirationCacheCalendar.time)
                .flatMap { expirationCacheDate ->
                    Single.fromCallable {
                        var count = 0L
                        File(GlideApp.getPhotoCacheDir(context)?.absolutePath)
                                .listFiles()
                                .forEach {
                                    val date = Date(it.lastModified())
                                    if (it.isFile && date.before(expirationCacheDate)) {
                                        if (it.delete()) {
                                            count++
                                        }
                                    }
                                }
                        count
                    }
                }
                .subscribe({}, {})
    }
}
