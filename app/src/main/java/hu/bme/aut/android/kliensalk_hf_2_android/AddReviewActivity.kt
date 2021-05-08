package hu.bme.aut.android.kliensalk_hf_2_android

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.data.Review
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityAddReviewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class AddReviewActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var binding: ActivityAddReviewBinding
    private lateinit var database: UserDatabase
    private var userId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = getString(R.string.add_review_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        database = UserDatabase.getDatabase(applicationContext)

        userId = intent.getLongExtra("userId", 0)

        binding.btnSaveReview.setOnClickListener {
            launch {
                database.userWithReviewsDao().insertReview(
                    Review(
                        userCreatorId = userId,
                        title = binding.etTitle.text.toString()
                    )
                )
            }
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}