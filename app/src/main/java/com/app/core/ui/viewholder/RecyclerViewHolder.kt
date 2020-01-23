package com.app.core.ui.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.core.R
import com.app.core.common.Utils
import com.app.core.model.RecyclerItem

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameTxtView: TextView = view.findViewById(R.id.title)
    private val descriptionTxtView: TextView = view.findViewById(R.id.subtitle)
    private var repo: RecyclerItem? = null

    init {
        view.setOnClickListener {
            Utils.showToast(view.context, "test")
        }
    }

    fun bind(recyclerItem: RecyclerItem?) {
        var descriptionVisibility = View.GONE
        if (recyclerItem == null) {
            val resources = itemView.resources
            nameTxtView.text = resources.getString(R.string.loading)
        } else {
            this.repo = recyclerItem
            nameTxtView.text = recyclerItem.name
            recyclerItem.description?.let {
                descriptionTxtView.text = recyclerItem.description
                descriptionVisibility = View.VISIBLE
            }
        }
        descriptionTxtView.visibility = descriptionVisibility
    }

    companion object {
        fun create(parent: ViewGroup): RecyclerViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
            return RecyclerViewHolder(view)
        }
    }
}