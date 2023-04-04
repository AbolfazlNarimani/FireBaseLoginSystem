package com.example.foroshgah_slami.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foroshgah_slami.databinding.ActivityUserProfileBinding

private lateinit var binding: ActivityUserProfileBinding
class UserProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}