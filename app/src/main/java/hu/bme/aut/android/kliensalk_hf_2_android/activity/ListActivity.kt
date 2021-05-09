package hu.bme.aut.android.kliensalk_hf_2_android.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.kliensalk_hf_2_android.MainActivity
import hu.bme.aut.android.kliensalk_hf_2_android.R
import hu.bme.aut.android.kliensalk_hf_2_android.adapter.ReviewAdapter
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.data.model.Review
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityListBinding
import kotlinx.coroutines.*


class ListActivity : AppCompatActivity(), ReviewAdapter.ReviewClickListener,
    CoroutineScope by MainScope() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: ReviewAdapter
    private lateinit var database: UserDatabase

    private val newReviewContract = registerForActivityResult(
        NewReviewActivity.NewReviewContract()
    ) { review ->
        if (review == null) {
            Toast.makeText(applicationContext, "not saved", Toast.LENGTH_LONG).show()
        } else {
            addItemInBackground(review)
        }
    }

    private val editReviewContract = registerForActivityResult(
        NewReviewActivity.EditReviewContract()
    ) { review ->
        if (review == null) {
            Toast.makeText(applicationContext, "not saved", Toast.LENGTH_LONG).show()
        } else {
            updateItemInBackground(review)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getDatabase(applicationContext)

        title = getString(R.string.reviews_title, intent.getStringExtra("username"))

        binding.fab.setOnClickListener {
            newReviewContract.launch(intent.getLongExtra("userId", 0))
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ReviewAdapter(this)
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = adapter
        loadItemsInBackground()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.logout_menu, menu)
        return true
    }

    private fun loadItemsInBackground() = launch {
        val items = withContext(Dispatchers.IO) {
            database.userWithReviewsDao().getReviewsForUser(intent.getLongExtra("userId", 0))
        }
        adapter.loadItems(items)
    }

    override fun onItemChanged(item: Review) {
        updateItemInBackground(item)
    }

    override fun onItemRemoved(item: Review) {
        deleteItemInBackground(item)
    }

    override fun onItemClicked(item: Review) {
        startActivity(Intent(this, ViewReview::class.java).putExtra("review", item))
    }

    override fun onItemModified(item: Review) {
        editReviewContract.launch(item)
    }

    private fun updateItemInBackground(item: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().updateReview(item)
        }
        adapter.update(item)
    }

    fun onShoppingItemCreated(item: Review) {
        addItemInBackground(item)
    }

    private fun addItemInBackground(item: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().insertReview(item)
        }
        adapter.addItem(item)
    }

    private fun deleteItemInBackground(item: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().deleteReview(item)
        }
        adapter.deleteItem(item)
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