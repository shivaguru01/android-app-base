package com.app.core.base

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.widget.ContentLoadingProgressBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.core.R
import com.app.core.common.Utils
import com.app.core.model.Action
import com.app.core.model.NetworkState

abstract class BaseRecyclerFragment<T : BaseRecyclerViewModel<I>, I> : BaseFragment<T>() {

    protected lateinit var recyclerView: RecyclerView
    protected lateinit var emptyTextView: TextView
    protected lateinit var contentLoadingProgressBar: ContentLoadingProgressBar


    override fun setUi() {
        recyclerView = view!!.findViewById(R.id.list)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        //emptyTextView = view!!.findViewById(R.id.emptyTextMsg)
        //contentLoadingProgressBar = view!!.findViewById(R.id.loader)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRecylerViewModel().loadContent(arguments)
    }

    override fun observeViewModel() {
        super.observeViewModel()
        getRecylerViewModel().let {
            it.getListItem().observe(viewLifecycleOwner, Observer { itemList ->
                onItemsUpdated(itemList)
                when (itemList?.size) {
                    null, 0 -> {
                        showEmptyMsgTxtView()
                    }
                    else -> {
                        showRecyclerView()
                        setAdapter(itemList)
                    }
                }
            })
        }
        setOnItemClick()
    }


    override fun handleNetworkState(networkState: NetworkState?) {
        Utils.setContentLoadingSettings(contentLoadingProgressBar, networkState)
    }

    override fun handleEvents(action: Action?) {
        when (action?.actionId) {
            BaseRecyclerViewModel.NO_ITEMS -> {
                showEmptyMsgTxtView()
            }
        }
    }

    abstract fun onItemsUpdated(itemList: List<I>?)

    abstract fun setAdapter(itemList: List<I>)

    private fun setOnItemClick() {
        getRecylerViewModel().getSelectedItem().observe(viewLifecycleOwner, Observer { selectedItem ->
            if (selectedItem != null) {
                onItemClicked(selectedItem)
            }
        })
    }

    private fun showEmptyMsgTxtView() {
        emptyTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showRecyclerView() {
        emptyTextView.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    protected abstract fun getRecylerViewModel(): T

    protected abstract fun onItemClicked(itemClicked: I)

    override fun getViewModel(): T = getRecylerViewModel()

}