package com.example.foroshgah_slami.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityRegisterBinding

private lateinit var binding: ActivityRegisterBinding
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding : ActivityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        /*setupActionBar()*/

        val tv_login = findViewById<TextView>(R.id.tv_login)
        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /*private fun setupActionBar() {

        setSupportActionBar(binding.toolbarRegisterActivity)
        val actsionBar = supportActionBar
        if (actsionBar != null) {
            actsionBar.setDisplayHomeAsUpEnabled(true)
            actsionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarRegisterActivity.setNavigationOnClickListener {onBackPressedDispatcher}
    }*/

}