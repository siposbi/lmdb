package hu.bme.aut.android.kliensalk_hf_2_android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.data.UserDatabase
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Kliensalkhf2android)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = UserDatabase.getDatabase(applicationContext)

        binding.btnLogin.setOnClickListener {
            when {
                binding.etUsername.text.toString().isEmpty() -> {
                    binding.etUsername.requestFocus()
                    binding.etUsername.error = getString(R.string.no_username_entered_error)
                }
                binding.etPassword.text.toString().isEmpty() -> {
                    binding.etPassword.requestFocus()
                    binding.etPassword.error = getString(R.string.no_password_entered_error)
                }
                else -> {
                    val username = binding.etUsername.text.toString()
                    val password = binding.etPassword.text.toString()
                    launch {
                        val userWithReviews =
                            database.userWithReviewsDao().login(username, password)
                        if (userWithReviews == null) {
                            runOnUiThread {
                                Toast.makeText(
                                    applicationContext,
                                    "Invalid Credentials",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            startActivity(Intent(this@MainActivity, ListActivity::class.java))
                        }
                    }
                }
            }
        }
    }
}