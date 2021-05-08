package hu.bme.aut.android.kliensalk_hf_2_android.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ReviewListItemBinding

class ReviewAdapter(private val listener: ReviewClickListener) :
    RecyclerView.Adapter<ReviewAdapter.ShoppingViewHolder>() {
    private val items = mutableListOf<Review>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ShoppingViewHolder(
        ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val current = items[position]

        holder.binding.tvTitle.text = current.title
        holder.binding.tvYear.text = current.year

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemRemoved(current)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(current)
        }
    }

    fun addItem(item: Review) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun update(shoppingItems: List<Review>) {
        items.clear()
        items.addAll(shoppingItems)
        notifyDataSetChanged()
    }

    fun deleteItem(item: Review) {
        val index = items.indexOf(item)
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun getItemCount(): Int = items.size

    interface ReviewClickListener {
        fun onItemChanged(item: Review)
        fun onItemRemoved(item: Review)
        fun onItemClicked(item: Review)
    }

    inner class ShoppingViewHolder(val binding: ReviewListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}