package com.app.core.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.core.model.RecyclerItem
import com.app.core.ui.viewholder.RecyclerViewHolder

class RecyclerAdapter : PagedListAdapter<RecyclerItem, RecyclerView.ViewHolder>(RECYCLER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecyclerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { recyclerItem ->
            (holder as RecyclerViewHolder).bind(recyclerItem)
        }
    }

    companion object {
        private val RECYCLER_COMPARATOR = object : DiffUtil.ItemCallback<RecyclerItem>() {
            override fun areItemsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RecyclerItem, newItem: RecyclerItem): Boolean =
                oldItem.name == newItem.name
        }
    }

}