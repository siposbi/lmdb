package hu.bme.aut.android.lmdb.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ActivityViewMovieBinding


class ViewMovie : AppCompatActivity() {
    companion object {
        const val KEY_VIEW_MOVIE_STRING = "KEY_VIEW_MOVIE_STRING"
    }

    private lateinit var binding: ActivityViewMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val movie = intent.getParcelableExtra<Movie>(KEY_VIEW_MOVIE_STRING)!!
        title = movie.title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val widthInPixels = applicationContext.resources.displayMetrics.widthPixels
        Picasso.get().load(movie.posterUrl).resize(widthInPixels, 0).into(binding.ivPoster)

        binding.etTitle.text = movie.title
        binding.etYear.text = movie.year
        binding.etGenre.text = movie.genre
        binding.etPlot.text = movie.plot
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}