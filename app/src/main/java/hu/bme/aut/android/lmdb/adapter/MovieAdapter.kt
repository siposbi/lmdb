package hu.bme.aut.android.lmdb.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.MovieListItemBinding

class MovieAdapter(private val listener: MovieClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val items = mutableListOf<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val current = items[position]

        holder.binding.tvTitle.text = current.title
        holder.binding.tvYear.text = current.year
        holder.binding.tvPlot.text = current.plot

        holder.binding.btnDelete.setOnClickListener {
            listener.onItemRemoved(current)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClicked(current)
        }

        holder.binding.btnEdit.setOnClickListener {
            listener.onItemModified(current)
        }
    }

    fun addItem(item: Movie) {
        items.add(0, item)
        notifyItemInserted(0)
    }

    fun loadItems(loadedItems: List<Movie>) {
        items.clear()
        items.addAll(loadedItems.sortedByDescending(Movie::movieId))
        notifyDataSetChanged()
    }

    fun update(item: Movie) {
        items.indexOfFirst { it.movieId == item.movieId }.also { index ->
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

    inner class MovieViewHolder(val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}