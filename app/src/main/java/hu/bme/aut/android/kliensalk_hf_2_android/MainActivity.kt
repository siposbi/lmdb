package hu.bme.aut.android.kliensalk_hf_2_android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.data.User
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Kliensalkhf2android)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        database = UserDatabase.getDatabase(applicationContext)
        title = getString(R.string.main_title)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        val loginId = sharedPreferences.getLong("userId", 0)
        val loginUsername = sharedPreferences.getString("username", "")

        if (isLoggedIn) {
            startActivity(createReviewIntent(loginId, loginUsername!!))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            if (checkInputFields().not()) {
                return@setOnClickListener
            }
            val username = binding.etUsername.text.toString().lowercase()
            val password = binding.etPassword.text.toString()
            launch {
                val user = database.userWithReviewsDao().login(username, password)
                if (user == null) {
                    showToast("Invalid Credentials")
                } else {
                    with(sharedPreferences.edit()) {
                        putBoolean("isLoggedIn", true)
                        putLong("userId", user.userId)
                        putString("username", user.username)
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
            val username = binding.etUsername.text.toString().lowercase()
            val password = binding.etPassword.text.toString()
            launch {
                if (database.userWithReviewsDao().checkIfExists(username) > 0) {
                    showToast("Username Exists")
                } else {
                    database.userWithReviewsDao()
                        .register(User(username = username, password = password))
                    showToast("Successfully Registered")
                }
            }
        }
    }

    private fun checkInputFields(): Boolean {
        if (binding.etUsername.text.toString().isEmpty()) {
            binding.etUsername.requestFocus()
            binding.etUsername.error = getString(R.string.no_username_entered_error)
        }
        if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.requestFocus()
            binding.etPassword.error = getString(R.string.no_password_entered_error)
        }
        return binding.etPassword.text.toString().isNotEmpty() && binding.etUsername.text.toString()
            .isNotEmpty()
    }

    private fun createReviewIntent(userId: Long, username: String): Intent {
        val intent = Intent(this@MainActivity, ListActivity::class.java)
        intent.putExtra("userId", userId)
        intent.putExtra("username", username)
        return intent
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
    }
}