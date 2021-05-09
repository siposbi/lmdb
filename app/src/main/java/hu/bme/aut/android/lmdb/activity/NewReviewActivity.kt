package hu.bme.aut.android.lmdb.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.lmdb.R
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ActivityNewReviewBinding
import hu.bme.aut.android.lmdb.network.NetworkManager
import hu.bme.aut.android.lmdb.network.model.OMDBData
import hu.bme.aut.android.lmdb.utils.isValid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewReviewBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.add_movie_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val review = intent.getParcelableExtra<Movie>("review")
        if (review != null) {
            initFroEdit(review)
        }

        binding.btnSearch.setOnClickListener {
            NetworkManager.getMovie(
                binding.etTitle.text.toString(),
                binding.etYear.text.toString().split("-")[0]
            )!!.enqueue(object : Callback<OMDBData?> {

                override fun onResponse(
                    call: Call<OMDBData?>,
                    response: Response<OMDBData?>
                ) {
                    Log.d("", "onResponse: " + response.code())
                    if (response.isSuccessful) {
                        when (response.body()!!.hasResponse) {
                            "True" -> {
                                binding.etTitle.setText(response.body()!!.title!!)
                                binding.etYear.setText(response.body()!!.year!!)
                                binding.etGenre.setText(response.body()!!.genre!!)
                                binding.etPlot.setText(response.body()!!.plot!!)
                                binding.etPosterUrl.setText(response.body()!!.poster!!)
                            }
                            "False" -> {
                                Toast.makeText(
                                    this@NewReviewActivity,
                                    "Error: " + response.body()!!.error!!,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        Toast.makeText(
                            this@NewReviewActivity,
                            "Error: " + response.message(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<OMDBData?>,
                    throwable: Throwable
                ) {
                    throwable.printStackTrace()
                    Toast.makeText(
                        this@NewReviewActivity,
                        "Network request error occurred, check LOG",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        }

        binding.btnSave.setOnClickListener {
            if (checkInputFields()) {
                val responseReview = Movie(
                    reviewId = review?.reviewId ?: 0,
                    userCreatorId = intent.getLongExtra("userId", 0),
                    title = binding.etTitle.text.toString(),
                    year = binding.etYear.text.toString(),
                    genre = binding.etGenre.text.toString(),
                    plot = binding.etPlot.text.toString(),
                    posterUrl = binding.etPosterUrl.text.toString()
                )

                setResult(RESULT_OK, Intent().putExtra("review", responseReview))
                finish()
            }
        }
    }

    private fun initFroEdit(movie: Movie) {
        binding.etTitle.setText(movie.title)
        binding.etYear.setText(movie.year)
        binding.etGenre.setText(movie.genre)
        binding.etPlot.setText(movie.plot)
        binding.etPosterUrl.setText(movie.posterUrl)
    }

    private fun checkInputFields(): Boolean {
        return binding.etTitle.isValid(R.string.no_title_entered_error) &&
                binding.etYear.isValid(R.string.no_year_entered_error) &&
                binding.etGenre.isValid(R.string.no_genre_entered_error) &&
                binding.etPlot.isValid(R.string.no_plot_entered_error) &&
                binding.etPosterUrl.isValid(R.string.no_poster_url_entered_error)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle("Discard changes?")
            setPositiveButton(
                "Discard"
            ) { _: DialogInterface, _: Int ->
                super.onBackPressed()
            }
            setNegativeButton("Keep Editing", null)
            show()
        }
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

    class NewReviewContract : ActivityResultContract<Long, Movie?>() {
        override fun createIntent(context: Context, input: Long?): Intent =
            Intent(context, NewReviewActivity::class.java).putExtra("userId", input)

        override fun parseResult(resultCode: Int, intent: Intent?): Movie? =
            intent?.getParcelableExtra("review")

    }

    class EditReviewContract : ActivityResultContract<Movie, Movie?>() {
        override fun createIntent(context: Context, input: Movie?): Intent =
            Intent(context, NewReviewActivity::class.java).putExtra("review", input)

        override fun parseResult(resultCode: Int, intent: Intent?): Movie? =
            intent?.getParcelableExtra("review")

    }
}