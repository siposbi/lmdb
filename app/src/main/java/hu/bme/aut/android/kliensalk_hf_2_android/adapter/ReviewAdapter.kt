package hu.bme.aut.android.kliensalk_hf_2_android.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.kliensalk_hf_2_android.R
import hu.bme.aut.android.kliensalk_hf_2_android.adapter.ReviewAdapter.*
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review

class ReviewAdapter : ListAdapter<Review, ReviewViewHolder>(ReviewComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.title)
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val wordItemView: TextView = itemView.findViewById(R.id.tvTitle)

        fun bind(text: String?) {
            wordItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): ReviewViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.review_list_item, parent, false)
                return ReviewViewHolder(view)
            }
        }
    }

    class ReviewComparator : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.title == newItem.title
        }
    }
}