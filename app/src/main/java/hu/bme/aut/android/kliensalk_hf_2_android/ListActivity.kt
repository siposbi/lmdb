package hu.bme.aut.android.kliensalk_hf_2_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import hu.bme.aut.android.kliensalk_hf_2_android.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}