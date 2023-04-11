package com.example.foroshgah_slami.ui.UI.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivitySettingsBinding
import com.example.foroshgah_slami.firestore.FirestoreClass
import com.example.foroshgah_slami.models.User
import com.example.foroshgah_slami.utils.Constants
import com.example.foroshgah_slami.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserDetails: User
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        binding.btnLogout.setOnClickListener(this@SettingsActivity)
        binding.tvEdit.setOnClickListener(this@SettingsActivity)
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        binding.toolbarSettingsActivity.setNavigationOnClickListener{onBackPressed()}
    }

    private fun getUserDetails() {
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getUserDetails(this@SettingsActivity)

    }

    fun userDetailSuccess(user: User) {
        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, binding.ivUserPhoto)
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"

        mUserDetails = user
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {

               R.id.btn_logout -> {
                   FirebaseAuth.getInstance().signOut()
                   val intent = Intent(this@SettingsActivity , LoginActivity::class.java)
                   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(intent)
                   finish()
               }

              R.id.tv_edit -> {
                  val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                  intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                  startActivity(intent)
              }

            }
        }
    }
}