package com.example.foroshgah_slami.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityMainBinding
import com.example.foroshgah_slami.utils.Constants
import java.util.zip.Inflater

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        binding.tvMain.text = "${getString(R.string.Hello)} $username"
    }
}