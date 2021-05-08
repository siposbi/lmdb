package hu.bme.aut.android.kliensalk_hf_2_android.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.kliensalk_hf_2_android.MainActivity
import hu.bme.aut.android.kliensalk_hf_2_android.R
import hu.bme.aut.android.kliensalk_hf_2_android.ReviewsApplication
import hu.bme.aut.android.kliensalk_hf_2_android.adapter.ReviewAdapter
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityListBinding
import hu.bme.aut.android.kliensalk_hf_2_android.viewmodel.ReviewViewModel
import hu.bme.aut.android.kliensalk_hf_2_android.viewmodel.WordViewModelFactory


class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding

    private val newWordActivityRequestCode = 1
    private val wordViewModel: ReviewViewModel by viewModels {
        WordViewModelFactory((application as ReviewsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        wordViewModel.updateAllWords(intent.getLongExtra("userId", 0))

        val recyclerView = binding.recyclerview
        val adapter = ReviewAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.submitList(it) }
        })

        binding.fab.setOnClickListener {
            val intent = Intent(this@ListActivity, NewReviewActivity::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewReviewActivity.EXTRA_REPLY)?.let {
                val word = Review(
                    userCreatorId = intent.getLongExtra("userId", 0),
                    title = it
                )
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                "not saved",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("username", "")
                    putBoolean("isLoggedIn", false)
                    putLong("userId", 0)
                    apply()
                }

                startActivity(Intent(this, MainActivity::class.java))

                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}