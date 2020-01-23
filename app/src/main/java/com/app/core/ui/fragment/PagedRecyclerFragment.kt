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
import com.app.core.adapter.RecyclerAdapter
import com.app.core.base.BaseFragment
import com.app.core.common.APP_TAG
import com.app.core.common.Utils
import com.app.core.common.Utils.afterTextChanged
import com.app.core.common.Utils.launchKeyboard
import com.app.core.model.Action
import com.app.core.model.NetworkState
import com.app.core.model.RecyclerItem
import com.app.core.model.ServerException
import com.app.core.viewmodel.RecyclerViewModel
import kotlinx.android.synthetic.main.recyclerview_search_layout.*
import javax.inject.Inject

class PagedRecyclerFragment : BaseFragment<RecyclerViewModel>() {

    @Inject
    lateinit var recyclerViewModel: RecyclerViewModel

    private val adapter = RecyclerAdapter()

    override fun getViewModel(): RecyclerViewModel = recyclerViewModel

    override fun getAppBarTitle(): String = getString(R.string.welcome)

    override fun getMenuResId(): Int = 0

    override fun onMenuItemClick(item: MenuItem?): Boolean = true

    override fun showNavigationBack() = false

    override fun handleNetworkState(networkState: NetworkState?) {}

    override fun handleEvents(action: Action?) {}

    override fun setUi() {
        recylerListView.setHasFixedSize(true)
        recylerListView.layoutManager = LinearLayoutManager(recylerListView.context)
        searchEditText.run {
            launchKeyboard()
            afterTextChanged {
                search(it)
            }
        }
        recylerListView.adapter = adapter
    }

    override fun observeViewModel() {
        super.observeViewModel()
        getViewModel().repos.observe(this, Observer<PagedList<RecyclerItem>> {
            Log.d(APP_TAG, "list: ${it?.size}")
            showEmptyList(it)
            adapter.submitList(it)
        })
        // need to connect to ecosystem
        getViewModel().networkErrors.observe(this, Observer<ServerException?> { ex ->
            ex?.let{
                Utils.showToast(context, it.httpErrorCode.toString())
            }
        })
    }

    private fun search(search: String) {
        search.trim().let { searchText ->
            if (searchText.isNotEmpty() && searchText.length > 2) {
                recylerListView.scrollToPosition(0)
                adapter.submitList(null)
                recyclerViewModel.searchRepo(searchText)
            }
        }
    }

    private fun showEmptyList(list: PagedList<RecyclerItem>) {
        when (list.size == 0) {
            true -> {
                emptyTextMsg.visibility = View.VISIBLE
                recylerListView.visibility = View.GONE
            }
            false -> {
                emptyTextMsg.visibility = View.GONE
                recylerListView.visibility = View.VISIBLE
            }
        }
    }

    override fun getFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.recyclerview_search_layout, container, false);


}