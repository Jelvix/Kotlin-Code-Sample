/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.domain

import android.content.Context
import android.net.Uri
import android.util.SparseArray
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.jelvix.jelvixkotlindemo.data.api.entity.Apod
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.manager.IApiManager
import com.jelvix.jelvixkotlindemo.data.manager.IDbManager
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

interface IApodInteractor {
    fun getPictureOfTheDay(): Single<MediaItem?>
}

class ApodInteractor @Inject constructor(
    private val apiManager: IApiManager,
    private val dbManager: IDbManager,
    private val applicationContext: Context
) : IApodInteractor {

    override fun getPictureOfTheDay(): Single<MediaItem?> {
        val date = Apod.apodDateFormat.format(Date())
        return dbManager.getMediaItemByNasaId(date)
            .toSingle()
            .onErrorResumeNext {
                apiManager.getPictureOfTheDay(date)
                    .map { MediaItem.mapFromApod(it) }
                    .flatMap { checkIsMediaItemWithYoutubeLink(it) }
                    .flatMap { mediaItem ->
                        dbManager.insertMediaItem(mediaItem)
                            .andThen(Single.just(mediaItem))
                    }
            }
    }

    private fun checkIsMediaItemWithYoutubeLink(mediaItem: MediaItem): Single<MediaItem> {
        val originUrl = mediaItem.originMedia?.originUrl
        val youtubeUrlRegex = Regex("(http|https)://(www\\.|m.|)youtube\\.com")
        return if (originUrl?.contains(youtubeUrlRegex) == true) {
            var extractor: YouTubeExtractor? = null
            Single.create<MediaItem> { singleEmitter ->
                val youtubeId = Uri.parse(originUrl).lastPathSegment
                extractor = object : YouTubeExtractor(applicationContext) {
                    public override fun onExtractionComplete(
                        ytFiles: SparseArray<YtFile>?,
                        vMeta: VideoMeta
                    ) {
                        if (ytFiles != null) {
                            mediaItem.originMedia?.originUrl = ytFiles.get(22).url
                            mediaItem.previewLink = vMeta.thumbUrl
                            singleEmitter.onSuccess(mediaItem)
                        } else {
                            singleEmitter.onSuccess(mediaItem)
                        }
                    }
                }
                extractor?.extract(youtubeId, false, false)
            }.doOnDispose {
                extractor?.cancel(true)
            }
        } else {
            Single.just(mediaItem)
        }
    }
}