package hu.bme.aut.android.lmdb.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ActivityViewMovieBinding


class ViewMovie : AppCompatActivity() {
    companion object {
        const val KEY_REVIEW_STRING = "KEY_REVIEW_STRING"
    }
    private lateinit var binding: ActivityViewMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val review = intent.getParcelableExtra<Movie>(KEY_REVIEW_STRING)!!

        title = review.title

        val widthInPixels = applicationContext.resources.displayMetrics.widthPixels

        Picasso.get().load(review.posterUrl).resize(widthInPixels, 0).into(binding.ivPoster)

        binding.etTitle.text = review.title
        binding.etYear.text = review.year
        binding.etGenre.text = review.genre
        binding.etPlot.text = review.plot
    }
}