package hu.bme.aut.android.kliensalk_hf_2_android.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import hu.bme.aut.android.kliensalk_hf_2_android.data.model.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityViewReviewBinding


class ViewReview : AppCompatActivity() {
    lateinit var binding: ActivityViewReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val review = intent.getParcelableExtra<Review>("review")!!

        title = review.title

        val widthInPixels = applicationContext.resources.displayMetrics.widthPixels

        Picasso.get().load(review.posterUrl).resize(widthInPixels, 0).into(binding.ivPoster)

        binding.etTitle.text = review.title
        binding.etYear.text = review.year
        binding.etGenre.text = review.genre
        binding.etPlot.text = review.plot
    }
}