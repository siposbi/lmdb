package hu.bme.aut.android.lmdb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.lmdb.activity.MovieListActivity
import hu.bme.aut.android.lmdb.activity.MovieListActivity.Companion.KEY_USERID_STRING
import hu.bme.aut.android.lmdb.activity.MovieListActivity.Companion.KEY_USERNAME_STRING
import hu.bme.aut.android.lmdb.data.UserDatabase
import hu.bme.aut.android.lmdb.data.model.User
import hu.bme.aut.android.lmdb.databinding.ActivityMainBinding
import hu.bme.aut.android.lmdb.utils.hideKeyboard
import hu.bme.aut.android.lmdb.utils.isValid
import hu.bme.aut.android.lmdb.utils.showSnackbar
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    companion object {
        const val KEY_IS_LOGGED_IN_STRING = "KEY_IS_LOGGED_IN_STRING"
        const val KEY_USER_PREFERENCES_STRING = "KEY_USER_PREFERENCES_STRING"
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LMDB)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        database = UserDatabase.getDatabase(applicationContext)
        setContentView(binding.root)

        title = getString(R.string.main_title)

        val sharedPreferences =
            getSharedPreferences(KEY_USER_PREFERENCES_STRING, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN_STRING, false)
        if (isLoggedIn) {
            val loginId = sharedPreferences.getLong(KEY_USERID_STRING, 0)
            val loginUsername = sharedPreferences.getString(KEY_USERNAME_STRING, "")
            startActivity(createReviewIntent(loginId, loginUsername!!))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            if (checkInputFields().not()) {
                return@setOnClickListener
            }

            hideKeyboard()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            launch {
                val user = database.userWithReviewsDao().login(username, password)
                if (user == null) {
                    showSnackbar(R.string.invalid_credentials_error)
                } else {
                    with(sharedPreferences.edit()) {
                        putBoolean(KEY_IS_LOGGED_IN_STRING, true)
                        putLong(KEY_USERID_STRING, user.userId)
                        putString(KEY_USERNAME_STRING, user.username)
                        apply()
                    }
                    startActivity(createReviewIntent(user.userId, user.username))
                    finish()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            if (checkInputFields().not()) {
                return@setOnClickListener
            }

            hideKeyboard()
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()
            launch {
                if (database.userWithReviewsDao().checkIfExists(username) > 0) {
                    showSnackbar(R.string.username_exists_error)
                } else {
                    database.userWithReviewsDao()
                        .register(User(username = username, password = password))
                    showSnackbar(R.string.successfully_registered_msg)
                }
            }
        }
    }

    private fun checkInputFields(): Boolean {
        return binding.etUsername.isValid(R.string.no_username_entered_error) &&
                binding.etPassword.isValid(R.string.no_password_entered_error)
    }

    private fun createReviewIntent(userId: Long, username: String): Intent {
        return Intent(this@MainActivity, MovieListActivity::class.java)
            .putExtra(KEY_USERID_STRING, userId)
            .putExtra(KEY_USERNAME_STRING, username)
    }
}