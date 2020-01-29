package com.app.core.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.core.model.RecyclerItemAlbum
import com.app.core.ui.viewholder.AlbumViewHolder

class AlbumAdapter : PagedListAdapter<RecyclerItemAlbum, RecyclerView.ViewHolder>(RECYCLER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlbumViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { recyclerItem ->
            (holder as AlbumViewHolder).bind(recyclerItem, position)
        }
    }

    companion object {
        private val RECYCLER_COMPARATOR = object : DiffUtil.ItemCallback<RecyclerItemAlbum>() {
            override fun areItemsTheSame(oldItem: RecyclerItemAlbum, newItem: RecyclerItemAlbum): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: RecyclerItemAlbum, newItem: RecyclerItemAlbum): Boolean =
                oldItem.title == newItem.title
        }
    }

}