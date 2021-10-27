package com.inventrohyder.will

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class WillListAdapter : ListAdapter<Will, WillListAdapter.WillViewHolder>(WillsComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WillViewHolder {
        return WillViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WillViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.will)
    }

    class WillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val willItemView: TextView = itemView.findViewById(R.id.textView)

        fun bind(text: String?) {
            willItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): WillViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return WillViewHolder(view)
            }
        }
    }

    class WillsComparator : DiffUtil.ItemCallback<Will>() {
        override fun areItemsTheSame(oldItem: Will, newItem: Will): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Will, newItem: Will): Boolean {
            return oldItem.will == newItem.will
        }
    }
}