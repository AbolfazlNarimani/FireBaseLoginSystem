package com.example.foroshgah_slami.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityRegisterBinding
import com.example.foroshgah_slami.firestore.FirestoreClass
import com.example.foroshgah_slami.models.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private lateinit var binding: ActivityRegisterBinding

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }



        val tv_login = findViewById<TextView>(R.id.tv_login)
        tv_login.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
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
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            binding.etPassword.text.toString()
                .trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),
                    true
                )
                false
            }

            !binding.cbTermsAndConditions.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agrre_terms_and_conditions),
                    true
                )
                false
            }
            else -> {
                //showErrorSnackBar(resources.getString(R.string.registery_successful), false)
                true
            }
        }
    }

    private fun registerUser() {

        // check with validate function if the entries are valid or not
        if (validateRegisterDetails()) {

            showProgressDialog(resources.getString(R.string.please_wait))

            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { Task ->

                    // if the registration is successfully done
                    if (Task.isSuccessful) {
                        // firebase registered user
                        val firebaseUser: FirebaseUser = Task.result!!.user!!

                        val user = User(
                            firebaseUser.uid,
                            binding.etFirstName.text.toString().trim {it <= ' '},
                            binding.etLastName.text.toString().trim {it <= ' '},
                            binding.etEmail.text.toString().trim {it <= ' '}
                        )

                        FirestoreClass().registerUser(this@RegisterActivity, userInfo = user)

                       // FirebaseAuth.getInstance().signOut()
                       // finish()
                    } else {
                        hideProgressDialog()
                        // if the registering is not successful then show the error message
                        showErrorSnackBar(Task.exception!!.message.toString(), true)
                    }
                }

        }

    }

    fun  userRegistrationSuccess() {

        hideProgressDialog()

        Toast.makeText(
            this@RegisterActivity,
            resources.getString(R.string.register_succsess),
            Toast.LENGTH_SHORT
        ).show()
    }

}