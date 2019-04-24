/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.Category
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.MainRecyclerViewAdapter
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.base.BaseComponentFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.main.presenter.MainPresenter
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class MainFragment : BaseComponentFragment(), IMainView {

    @Inject
    lateinit var daggerPresenter: Lazy<MainPresenter>
    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    protected fun provideMainPresenter(): MainPresenter = daggerPresenter.get()

    override val layoutResId = R.layout.fragment_main

    val adapter by lazy { MainRecyclerViewAdapter(presenter.categories, mainRecyclerClickListener) }


    /* lifecycle */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        recyclerView.adapter = adapter
    }


    /* public functions */

    override fun onPictureOfTheDayLoaded(mediaItem: MediaItem) {
        adapter.onMediaTick(mediaItem, ScreenType.APOD)
    }

    override fun onMediaImageTick(mediaItem: MediaItem) {
        adapter.onMediaTick(mediaItem, ScreenType.IMAGE)
    }

    override fun onMediaVideoTick(mediaItem: MediaItem) {
        adapter.onMediaTick(mediaItem, ScreenType.VIDEO)
    }

    override fun onMediaFavoriteTick(mediaItem: MediaItem) {
        adapter.onMediaTick(mediaItem, ScreenType.FAVORITE)
    }

    override fun onTotalCountLoaded(totalCount: Int, screenType: ScreenType) {
        adapter.onTotalCountLoaded(totalCount, screenType)
    }


    /* callbacks */

    private val mainRecyclerClickListener = object : MainRecyclerViewAdapter.IMainRecyclerClickListener {
        override fun onItemClick(category: Category?) {
            category?.screenType ?: return
            val action = when(category.screenType) {
                ScreenType.APOD -> {
                    val media = category.lastShowedMediaItem ?: return
                    MainFragmentDirections.actionMainFragmentToMediaDetailFragment(media)
                }
                ScreenType.IMAGE -> {
                    category.initialMediaItems = presenter.initialMediaImages
                    MainFragmentDirections.actionMainFragmentToSearchFragment(category)
                }
                ScreenType.VIDEO -> {
                    category.initialMediaItems = presenter.initialMediaVideos
                    MainFragmentDirections.actionMainFragmentToSearchFragment(category)
                }
                ScreenType.FAVORITE -> {
                    category.initialMediaItems = presenter.initialMediaFavorites
                    MainFragmentDirections.actionMainFragmentToSearchFragment(category)
                }
            }
            NavHostFragment.findNavController(this@MainFragment).navigate(action)
        }
    }
}
