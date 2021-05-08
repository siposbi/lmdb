package hu.bme.aut.android.kliensalk_hf_2_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ReviewListItemBinding

class ReviewAdapter(private val listener: ReviewClickListener) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var items = mutableListOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ReviewViewHolder(
        ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = items[position]

        holder.binding.tvTitle.text = review.title
    }

    fun addReview(review: Review) {
        items.add(review)
        notifyItemInserted(items.size - 1)
    }

    fun updateReviews(reviewList: List<Review>) {
        items.clear()
        items.addAll(reviewList)
        notifyDataSetChanged()
    }

    fun deleteReview(review: Review){
        val index = items.indexOf(review)
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun setData(newData: MutableList<Review>) {
        items = newData
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    interface ReviewClickListener {
        fun onReviewChanged(review: Review)
        fun onReviewRemoved(review: Review)
    }

    inner class ReviewViewHolder(val binding: ReviewListItemBinding) : RecyclerView.ViewHolder(binding.root)
}