package hu.bme.aut.android.kliensalk_hf_2_android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Kliensalkhf2android)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                    startActivity(Intent(this, ListActivity::class.java))
                }
            }
        }
    }
}