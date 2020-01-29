package com.app.core.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.app.core.BR
import com.app.core.R
import com.app.core.common.Utils
import com.app.core.model.RecyclerItemAlbum

class AlbumViewHolder(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

    private val root = binding.root
    private val nameTxtView: TextView = root.findViewById(R.id.title)
    private val descriptionTxtView: TextView = root.findViewById(R.id.subtitle)
    private val iconView: ImageView = root.findViewById(R.id.icon)

    init {
        root.setOnClickListener {
            Utils.showToast(root.context, "test")
        }
    }

    fun bind(recyclerItem: RecyclerItemAlbum?, position: Int?) {
        binding.setVariable(BR.album, recyclerItem )
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): AlbumViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                R.layout.recycler_item_album, parent, false
            )
            return AlbumViewHolder(binding)
        }
    }


}