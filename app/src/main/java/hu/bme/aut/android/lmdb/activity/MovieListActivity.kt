package hu.bme.aut.android.lmdb.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.lmdb.MainActivity
import hu.bme.aut.android.lmdb.MainActivity.Companion.KEY_IS_LOGGED_IN_STRING
import hu.bme.aut.android.lmdb.MainActivity.Companion.KEY_USER_PREFERENCES_STRING
import hu.bme.aut.android.lmdb.R
import hu.bme.aut.android.lmdb.activity.ViewMovie.Companion.KEY_REVIEW_STRING
import hu.bme.aut.android.lmdb.adapter.MovieAdapter
import hu.bme.aut.android.lmdb.data.UserDatabase
import hu.bme.aut.android.lmdb.data.model.Movie
import hu.bme.aut.android.lmdb.databinding.ActivityMovieListBinding
import hu.bme.aut.android.lmdb.utils.showSnackbar
import kotlinx.coroutines.*


class MovieListActivity : AppCompatActivity(), MovieAdapter.MovieClickListener,
    CoroutineScope by MainScope() {
    companion object {
        const val KEY_USERNAME_STRING = "KEY_USERNAME_STRING"
        const val KEY_USERID_STRING = "KEY_USERID_STRING"
    }

    private lateinit var binding: ActivityMovieListBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var database: UserDatabase

    private val newReviewContract =
        registerForActivityResult(NewMovieActivity.NewMovieContract()) { review ->
            if (review == null) {
//                Snackbar.make(binding.root, "a", Snackbar.LENGTH_LONG).show()
                showSnackbar(R.string.movie_not_saved_msg, binding.root)
            } else {
                addItemInBackground(review)
            }
        }

    private val editReviewContract =
        registerForActivityResult(NewMovieActivity.EditMovieContract()) { review ->
            if (review == null) {
                showSnackbar(R.string.movie_not_edited_msg, binding.root)
            } else {
                updateItemInBackground(review)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieListBinding.inflate(layoutInflater)
        database = UserDatabase.getDatabase(applicationContext)
        setContentView(binding.root)

        title = getString(R.string.list_title, intent.getStringExtra(KEY_USERNAME_STRING))

        binding.fab.setOnClickListener {
            newReviewContract.launch(intent.getLongExtra(KEY_USERID_STRING, 0))
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = MovieAdapter(this)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        loadItemsInBackground()
    }

    override fun onItemChanged(item: Movie) {
        updateItemInBackground(item)
    }

    override fun onItemClicked(item: Movie) {
        startActivity(Intent(this, ViewMovie::class.java).putExtra(KEY_REVIEW_STRING, item))
    }

    override fun onItemModified(item: Movie) {
        editReviewContract.launch(item)
    }

    override fun onItemRemoved(item: Movie) {
        deleteItemInBackground(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                val sharedPreferences =
                    getSharedPreferences(KEY_USER_PREFERENCES_STRING, Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString(KEY_USERNAME_STRING, "")
                    putBoolean(KEY_IS_LOGGED_IN_STRING, false)
                    putLong(KEY_USERID_STRING, 0)
                    apply()
                }

                startActivity(Intent(this, MainActivity::class.java))

                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    private fun addItemInBackground(item: Movie) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().insertReview(item)
        }
        showSnackbar(R.string.movie_saved_msg, binding.root)
        adapter.addItem(item)
    }

    private fun loadItemsInBackground() = launch {
        val items = withContext(Dispatchers.IO) {
            database.userWithReviewsDao()
                .getReviewsForUser(intent.getLongExtra(KEY_USERID_STRING, 0))
        }
        adapter.loadItems(items)
    }

    private fun updateItemInBackground(item: Movie) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().updateReview(item)
        }
        showSnackbar(R.string.movie_edited_msg, binding.root)
        adapter.update(item)
    }

    private fun deleteItemInBackground(item: Movie) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().deleteReview(item)
        }
        adapter.deleteItem(item)
    }
}