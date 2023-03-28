package com.example.foroshgah_slami.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityRegisterBinding

private lateinit var binding: ActivityRegisterBinding
class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        val tv_login = findViewById<TextView>(R.id.tv_login)
        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
        }

    }

    private fun setupActionBar() {
        val toolbarRegisterActivity = binding.toolbarRegisterActivity
        setSupportActionBar(toolbarRegisterActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }


    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim {it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }

            binding.etPassword.text.toString().trim {it <= ' '} != binding.etConfirmPassword.text.toString().trim {it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }

            !binding.cbTermsAndConditions.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agrre_terms_and_conditions), true)
                false
            }
            else -> {
                showErrorSnackBar(resources.getString(R.string.registery_successful) , false)
                true
            }
        }
    }
}