package hu.bme.aut.android.kliensalk_hf_2_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.kliensalk_hf_2_android.adapter.ReviewAdapter
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityListBinding
import hu.bme.aut.android.kliensalk_hf_2_android.fragments.NewReviewDialogFragment
import kotlinx.coroutines.*


class ListActivity : AppCompatActivity(),
    ReviewAdapter.ReviewClickListener,
    NewReviewDialogFragment.NewReviewDialogListener,
    CoroutineScope by MainScope() {
    private lateinit var binding: ActivityListBinding
    lateinit var database: UserDatabase
    private lateinit var adapter: ReviewAdapter

    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra("username")
        title = getString(R.string.reviews_title, username)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        database = UserDatabase.getDatabase(applicationContext)

        userId = intent.getLongExtra("userId", 0)

        binding.fab.setOnClickListener {

            val fragment = NewReviewDialogFragment()
            val bundle = Bundle()
            bundle.putLong("userId", userId)
            fragment.arguments = bundle
            fragment.show(
                supportFragmentManager,
                NewReviewDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ReviewAdapter(this)
        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = adapter
        loadReviewsInBackground()
    }

    private fun loadReviewsInBackground() = launch {
        val reviews = withContext(Dispatchers.IO) {
            database.userWithReviewsDao().getUserData(userId).reviews
        }
        adapter.updateReviews(reviews)
    }

    override fun onReviewChanged(review: Review) {
        updateReviewInBackground(review)
    }

    override fun onReviewRemoved(review: Review) {
        deleteReviewInBackground(review)
    }

    private fun updateReviewInBackground(review: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().updateReview(review)
        }
    }

    private fun addReviewInBackground(review: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().insertReview(review)
        }
        adapter.addReview(review)
    }

    private fun deleteReviewInBackground(review: Review) = launch {
        withContext(Dispatchers.IO) {
            database.userWithReviewsDao().deleteReview(review)
        }
        adapter.deleteReview(review)
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

    override fun onShoppingItemCreated(item: Review) {
        addReviewInBackground(item)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}