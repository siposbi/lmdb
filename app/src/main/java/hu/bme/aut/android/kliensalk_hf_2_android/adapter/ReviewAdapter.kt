package hu.bme.aut.android.kliensalk_hf_2_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.kliensalk_hf_2_android.adapter.ReviewAdapter.*
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ReviewListItemBinding

class ReviewAdapter : ListAdapter<Review, ReviewViewHolder>(ReviewComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val current = getItem(position)

        holder.binding.tvTitle.text = current.title
        holder.binding.tvYear.text = current.year

        holder.binding.ibRemove.setOnClickListener {

        }
    }

    inner class ReviewViewHolder(val binding: ReviewListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ReviewComparator : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.title == newItem.title
        }
    }
}