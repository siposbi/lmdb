package hu.bme.aut.android.lmdb.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.lmdb.R
import hu.bme.aut.android.lmdb.activity.MovieListActivity.Companion.KEY_USERID_STRING
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ActivityNewMovieBinding
import hu.bme.aut.android.lmdb.network.NetworkManager
import hu.bme.aut.android.lmdb.network.model.OMDBData
import hu.bme.aut.android.lmdb.utils.hideKeyboard
import hu.bme.aut.android.lmdb.utils.isValid
import hu.bme.aut.android.lmdb.utils.showSnackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewMovieActivity : AppCompatActivity() {
    companion object {
        const val KEY_MOVIE_STRING = "KEY_MOVIE_STRING"
    }

    lateinit var binding: ActivityNewMovieBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.add_movie_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val movie = intent.getParcelableExtra<Movie>(KEY_MOVIE_STRING)
        if (movie != null) {
            initFromEdit(movie)
            title = getString(R.string.edit_movie_title, movie.title)
        }

        binding.btnSearch.setOnClickListener {
            if (binding.etTitle.isValid(R.string.no_title_entered_error)) {
                initSearch()
                hideKeyboard()
            }
        }

        binding.btnSave.setOnClickListener {
            if (checkInputFields()) {
                val responseMovie = Movie(
                    movieId = movie?.movieId ?: 0,
                    userCreatorId = intent.getLongExtra(KEY_USERID_STRING, 0),
                    title = binding.etTitle.text.toString(),
                    year = binding.etYear.text.toString(),
                    genre = binding.etGenre.text.toString(),
                    plot = binding.etPlot.text.toString(),
                    posterUrl = binding.etPosterUrl.text.toString()
                )

                setResult(RESULT_OK, Intent().putExtra(KEY_MOVIE_STRING, responseMovie))
                finish()
            }
        }
    }

    private fun initSearch() {
        NetworkManager.getMovie(
            binding.etTitle.text.toString(),
            binding.etYear.text.toString().split("-")[0]
        )!!.enqueue(object : Callback<OMDBData?> {
            override fun onResponse(call: Call<OMDBData?>, response: Response<OMDBData?>) {
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
                            showSnackbar(response.body()!!.error!!)
                        }
                    }
                } else {
                    showSnackbar(getString(R.string.network_error, response.message()))
                }
            }

            override fun onFailure(call: Call<OMDBData?>, throwable: Throwable) {
                showSnackbar(R.string.network_error)
            }
        })
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)

        with(builder)
        {
            setTitle(R.string.discard_changes_alert_title)
            setPositiveButton(R.string.positive_button_text) { _: DialogInterface, _: Int -> super.onBackPressed() }
            setNegativeButton(R.string.negative_button_text, null)
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

    private fun initFromEdit(movie: Movie) {
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

    class NewMovieContract : ActivityResultContract<Long, Movie?>() {
        override fun createIntent(context: Context, input: Long?): Intent =
            Intent(context, NewMovieActivity::class.java).putExtra(KEY_USERID_STRING, input)

        override fun parseResult(resultCode: Int, intent: Intent?): Movie? =
            intent?.getParcelableExtra(KEY_MOVIE_STRING)

    }

    class EditMovieContract : ActivityResultContract<Movie, Movie?>() {
        override fun createIntent(context: Context, input: Movie?): Intent =
            Intent(context, NewMovieActivity::class.java).putExtra(KEY_MOVIE_STRING, input)

        override fun parseResult(resultCode: Int, intent: Intent?): Movie? =
            intent?.getParcelableExtra(KEY_MOVIE_STRING)

    }
}