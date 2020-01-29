package com.app.core.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.core.R
import com.app.core.adapter.AlbumAdapter
import com.app.core.adapter.RecyclerAdapter
import com.app.core.base.BaseFragment
import com.app.core.common.APP_TAG
import com.app.core.common.Utils
import com.app.core.common.Utils.afterTextChanged
import com.app.core.common.Utils.launchKeyboard
import com.app.core.model.*
import com.app.core.viewmodel.AlbumViewModel
import kotlinx.android.synthetic.main.recyclerview_search_layout.*
import javax.inject.Inject

class PagedRecyclerFragment : BaseFragment<AlbumViewModel>() {

    @Inject
    lateinit var albumViewModel: AlbumViewModel

    private val albumAdapter = AlbumAdapter()

    override fun getViewModel(): AlbumViewModel = albumViewModel

    override fun getAppBarTitle(): String = getString(R.string.welcome)

    override fun getMenuResId(): Int = 0

    override fun onMenuItemClick(item: MenuItem?): Boolean = true

    override fun showNavigationBack() = false

    override fun handleNetworkState(networkState: NetworkState?) {}

    override fun handleEvents(action: Action?) {}

    override fun setUi() {
        recylerListView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(recylerListView.context)
            adapter = albumAdapter
        }
        searchEditText.run {
            launchKeyboard()
            afterTextChanged {
                //search(it)
            }
        }
    }

    override fun observeViewModel() {
        super.observeViewModel()
        getViewModel().repos.observe(this, Observer<PagedList<RecyclerItemAlbum?>> {
            Log.d(APP_TAG, "list: ${it?.size}")
            showEmptyList(it)
            albumAdapter.submitList(it)
        })
        // need to connect to ecosystem
        getViewModel().networkErrors.observe(this, Observer<ServerException?> { ex ->
            ex?.let {
                Utils.showToast(context, it.httpErrorCode.toString())
            }
        })
    }

    private fun search(search: String) {
        search.trim().let { searchText ->
            if (searchText.isNotEmpty() && searchText.length > 2) {

            }
        }
    }

    private fun showEmptyList(list: PagedList<RecyclerItemAlbum?>) {
        when (list.size == 0) {
            true -> {
                emptyTextMsg.visibility = View.VISIBLE
                recylerListView.visibility = View.GONE
            }
            false -> {
                recylerListView.visibility = View.VISIBLE
                emptyTextMsg.visibility = View.GONE
            }
        }
    }

    override fun getFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.recyclerview_search_layout, container, false);


}