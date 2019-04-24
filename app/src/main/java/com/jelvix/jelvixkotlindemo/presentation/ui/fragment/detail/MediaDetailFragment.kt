/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail

import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.SingleSampleMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.MimeTypes
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.video.VideoListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.OriginMedia
import com.jelvix.jelvixkotlindemo.data.enums.MediaType
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.base.BaseComponentFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.detail.presenter.MediaDetailPresenter
import com.jelvix.jelvixkotlindemo.presentation.util.DateTimeUtils
import com.jelvix.jelvixkotlindemo.presentation.util.ext.loadDetailedMediaImage
import com.jelvix.jelvixkotlindemo.presentation.util.ext.loadDetailedMediaPreview
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_media_detail.*
import kotlinx.android.synthetic.main.partial_media_detail_bottom_sheet.*
import kotlinx.android.synthetic.main.stub_photo_view.*
import kotlinx.android.synthetic.main.stub_video_view.*
import javax.inject.Inject

class MediaDetailFragment : BaseComponentFragment(), IMediaDetailView {

    @Inject
    lateinit var daggerPresenter: Lazy<MediaDetailPresenter>
    @InjectPresenter
    lateinit var presenter: MediaDetailPresenter

    @ProvidePresenter
    protected fun provideMediaDetailPresenter(): MediaDetailPresenter = daggerPresenter.get()

    override val layoutResId = R.layout.fragment_media_detail

    val args: MediaDetailFragmentArgs by navArgs()

    private var behavior: BottomSheetBehavior<View>? = null

    private var player: SimpleExoPlayer? = null
    private var mediaSource: MediaSource? = null


    /* lifecycle */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = args.media.title
        toolbar.setNavigationIcon(R.drawable.ic_left)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        args.media.apply {
            previewImageView.loadDetailedMediaPreview(previewLink)
            titleTextView.text = title
            descriptionTextView.text = description
            dateCreationTextView.text = DateTimeUtils.formatRelativeDays(dateCreated)
        }
        behavior = BottomSheetBehavior.from(bottomSheetViewGroup)

        titleBottomSheetViewGroup.setOnClickListener {
            if (behavior?.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                behavior?.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        setHasOptionsMenu(true)
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            player?.playWhenReady = false
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            player?.playWhenReady = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        mediaSource = null
        player = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_media_detail, menu)
        val addToFavoritesMenuItem = menu?.findItem(R.id.addToFavoriteMenuItem)
        val removeFromFavoritesMenuItem = menu?.findItem(R.id.removeFromFavoriteMenuItem)
        addToFavoritesMenuItem?.isVisible = false
        removeFromFavoritesMenuItem?.isVisible = false
        when(presenter.isItemInFavorites) {
            true -> { removeFromFavoritesMenuItem?.isVisible = true }
            false -> { addToFavoritesMenuItem?.isVisible = true }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.addToFavoriteMenuItem -> {
                presenter.addMediaItemToFavorites()
                true
            }
            R.id.removeFromFavoriteMenuItem -> {
                presenter.removeMediaItemFromFavorites()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    /* public functions */

    override fun onOriginMediaLoaded(originMedia: OriginMedia) {
        when(originMedia.mediaType) {
            MediaType.IMAGE -> {
                viewStub.layoutResource = R.layout.stub_photo_view
                viewStub.inflate()
                photoView.maximumScale = 20f
                photoView.loadDetailedMediaImage(originMedia.originUrl) {
                    previewImageView.visibility = View.GONE
                }
            }
            MediaType.VIDEO -> {
                viewStub.layoutResource = R.layout.stub_video_view
                viewStub.inflate()
                initPlayer()
            }
            null -> {}
        }
    }

    override fun notifyOptionsMenu() {
        activity?.invalidateOptionsMenu()
    }


    /* private functions */

    private fun initPlayer() {
        val originMedia = presenter.mediaItem.originMedia ?: return
        if (originMedia.mediaType != MediaType.VIDEO) return
        player = ExoPlayerFactory.newSimpleInstance(DefaultRenderersFactory(context), DefaultTrackSelector(), DefaultLoadControl())

        player?.addVideoListener(object: VideoListener {
            override fun onVideoSizeChanged(width: Int, height: Int, unappliedRotationDegrees: Int, pixelWidthHeightRatio: Float) {}
            override fun onRenderedFirstFrame() {
                previewImageView.visibility = View.GONE
            }
        })
        // Build the video MediaSource
        val uri = Uri.parse(originMedia.originUrl)
        val userAgent = Util.getUserAgent(context, context?.getString(R.string.app_name))
        val httpDataSourceFactory = DefaultHttpDataSourceFactory(
            userAgent,
            null ,
            DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
            DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
            true
        )
        val dataSourceFactory = DefaultDataSourceFactory(
            context, null,
            httpDataSourceFactory
        )
        val videoSource = ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri)

        // Build the subtitle MediaSource
        if (originMedia.subtitleUrl != null) {
            val subtitleFormat = Format.createTextSampleFormat(null, MimeTypes.APPLICATION_SUBRIP, Format.NO_VALUE,null)
            val subtitleUri = Uri.parse(originMedia.subtitleUrl)
            val subtitleSource = SingleSampleMediaSource.Factory(dataSourceFactory).createMediaSource(subtitleUri, subtitleFormat, C.TIME_UNSET)
            mediaSource = MergingMediaSource(videoSource, subtitleSource)
        } else {
            mediaSource = videoSource
        }

        player?.prepare(mediaSource, true, true)
        playerView?.player = player
        player?.playWhenReady = true
    }
}
