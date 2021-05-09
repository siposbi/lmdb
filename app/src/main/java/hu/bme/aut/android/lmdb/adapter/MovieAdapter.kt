package hu.bme.aut.android.lmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ReviewListItemBinding

class MovieAdapter(private val listener: MovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val items = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        ReviewListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = items[position]

        holder.binding.tvTitle.text = current.title
        holder.binding.tvYear.text = current.year

        holder.binding.ibRemove.setOnClickListener {
            listener.onItemRemoved(current)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(current)
        }

        holder.binding.ibEdit.setOnClickListener {
            listener.onItemModified(current)
        }
    }

    fun addItem(item: Movie) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun loadItems(loadedItems: List<Movie>) {
        items.clear()
        items.addAll(loadedItems)
        notifyDataSetChanged()
    }

    fun update(item: Movie) {
        items.indexOfFirst { it.reviewId == item.reviewId }.also { index ->
            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun deleteItem(item: Movie) {
        items.indexOf(item).also { index ->
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    override fun getItemCount(): Int = items.size

    interface MovieClickListener {
        fun onItemChanged(item: Movie)
        fun onItemRemoved(item: Movie)
        fun onItemClicked(item: Movie)
        fun onItemModified(item: Movie)
    }

    inner class MovieViewHolder(val binding: ReviewListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}