package com.example.foroshgah_slami.ui.UI.activities

import android.os.Bundle
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityMainBinding
import com.example.foroshgah_slami.utils.Constants

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sharedPreferences = getSharedPreferences(Constants.MYSHOPPAL_PREFERENCES, MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        binding.tvMain.text = "${getString(R.string.Hello)} $username"

    }

}