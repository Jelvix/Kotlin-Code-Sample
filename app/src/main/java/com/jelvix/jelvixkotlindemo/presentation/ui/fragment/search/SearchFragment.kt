/**
 * Jelvix demo CodeSample
 * Copyright © 2019 Jelvix. All rights reserved.
 */

package com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jelvix.jelvixkotlindemo.R
import com.jelvix.jelvixkotlindemo.data.entity.MediaItem
import com.jelvix.jelvixkotlindemo.data.enums.ScreenType
import com.jelvix.jelvixkotlindemo.presentation.ui.adapter.MediaRecyclerViewAdapter
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.base.BaseComponentFragment
import com.jelvix.jelvixkotlindemo.presentation.ui.fragment.search.presenter.SearchPresenter
import com.jelvix.jelvixkotlindemo.presentation.util.pagination.PaginationHelper
import dagger.Lazy
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : BaseComponentFragment(), ISearchView {

    @Inject
    lateinit var daggerPresenter: Lazy<SearchPresenter>
    @InjectPresenter
    lateinit var presenter: SearchPresenter

    @ProvidePresenter
    protected fun provideSearchPresenter(): SearchPresenter = daggerPresenter.get()

    override val layoutResId = R.layout.fragment_search

    val args: SearchFragmentArgs by navArgs()

    val adapter by lazy { MediaRecyclerViewAdapter(presenter.items, mediaRecyclerClickListener) }
    private var paginationHelper: PaginationHelper? = null


    /* lifecycle */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_left)
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
        toolbar.title = when(args.category.screenType) {
            ScreenType.IMAGE -> resources.getString(R.string.images)
            ScreenType.VIDEO -> resources.getString(R.string.videos)
            ScreenType.FAVORITE -> resources.getString(R.string.favorites)
            else -> ""
        }
        paginationHelper = PaginationHelper()
        recyclerView.adapter = adapter
        paginationHelper?.initialize(recyclerView, swipeRefreshLayout)
        paginationHelper?.startLoading(presenter.paginationInfo)
        swipeRefreshLayout.setOnRefreshListener {
            paginationHelper?.reset()
        }
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paginationHelper?.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search, menu)
        val myActionMenuItem = menu?.findItem(R.id.action_search)
        val searchView = myActionMenuItem?.actionView as? SearchView
        searchView?.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { presenter.sendNewQuery(it) }
                return false
            }
        })
        searchView?.setOnCloseListener {
            presenter.sendNewQuery("")
            false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    /* public functions */

    override fun onDataLoaded(items: List<MediaItem>) {
        adapter.mergeNewItemList(items)
    }

    override fun onMediaItemDeleted(item: MediaItem) {
        adapter.removeItem(item)
    }

    override fun restartPagination() {
        paginationHelper?.reset(false)
        adapter.clearItems()
    }

    override fun onResetPagination() {
        paginationHelper?.checkRefreshFinishing()
    }


    /* callbacks */

    private val mediaRecyclerClickListener = object : MediaRecyclerViewAdapter.IMediaRecyclerClickListener {
        override fun onItemClick(media: MediaItem, view: View) {
            val action = SearchFragmentDirections.actionSearchFragmentToMediaDetailFragment(media)
            findNavController(this@SearchFragment).navigate(action)
        }
    }
}
