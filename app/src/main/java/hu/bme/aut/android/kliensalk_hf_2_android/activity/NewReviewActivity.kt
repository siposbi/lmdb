package hu.bme.aut.android.kliensalk_hf_2_android.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.R
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityNewReviewBinding
import hu.bme.aut.android.kliensalk_hf_2_android.model.OMDBData
import hu.bme.aut.android.kliensalk_hf_2_android.network.NetworkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewReviewActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewReviewBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.add_review_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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
                                binding.etPosterIrl.setText(response.body()!!.poster!!)
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
            val replyIntent = Intent()
            if (checkInputFields().not()) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val title = binding.etTitle.text.toString()
                replyIntent.putExtra("title", title)
                val year = binding.etYear.text.toString()
                replyIntent.putExtra("year", year)
                val genre = binding.etGenre.text.toString()
                replyIntent.putExtra("genre", genre)
                val plot = binding.etPlot.text.toString()
                replyIntent.putExtra("plot", plot)
                val posterUrl = binding.etPosterIrl.text.toString()
                replyIntent.putExtra("posterUrl", posterUrl)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    private fun checkInputFields(): Boolean {
        if (binding.etTitle.text.toString().isEmpty()) {
            binding.etTitle.requestFocus()
            binding.etTitle.error = getString(R.string.no_title_entered)
        }
        if (binding.etYear.text.toString().isEmpty()) {
            binding.etYear.requestFocus()
            binding.etYear.error = getString(R.string.no_year_entered)
        }
        if (binding.etGenre.text.toString().isEmpty()) {
            binding.etGenre.requestFocus()
            binding.etGenre.error = getString(R.string.no_genre_entered)
        }
        if (binding.etPlot.text.toString().isEmpty()) {
            binding.etPlot.requestFocus()
            binding.etPlot.error = getString(R.string.no_plot_entered)
        }
        if (binding.etPosterIrl.text.toString().isEmpty()) {
            binding.etPosterIrl.requestFocus()
            binding.etPosterIrl.error = getString(R.string.no_etposter_entered)
        }
        return binding.etTitle.text.toString().isNotEmpty() &&
                binding.etYear.text.toString().isNotEmpty() &&
                binding.etGenre.text.toString().isNotEmpty() &&
                binding.etPlot.text.toString().isNotEmpty() &&
                binding.etPosterIrl.text.toString().isNotEmpty()
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
}