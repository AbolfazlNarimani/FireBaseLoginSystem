package com.example.foroshgah_slami.ui.UI.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.foroshgah_slami.R
import com.example.foroshgah_slami.databinding.ActivityUserProfileBinding
import com.example.foroshgah_slami.firestore.FirestoreClass
import com.example.foroshgah_slami.models.User
import com.example.foroshgah_slami.utils.Constants
import com.example.foroshgah_slami.utils.GlideLoader
import java.io.IOException

private lateinit var binding: ActivityUserProfileBinding
private lateinit var mUserDetails: User
private var mSelectedImageUri: Uri? = null
private var mUserProfileImageURL: String = ""

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
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

        var userDetails: User = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etFirstName.setText(mUserDetails.firstName)
        binding.etLastName.setText(mUserDetails.lastName)
        binding.etEmail.setText(mUserDetails.email)

        if (mUserDetails.profileCompleted == 0) {
            binding.tvTitle.text = resources.getString(R.string.title_compete_profile)

            binding.etFirstName.isEnabled = false
            binding.etLastName.isEnabled = false
            binding.etEmail.isEnabled = false

        } else {

            binding.tvTitle.text = getString(R.string.Edit_profile)
            GlideLoader(this@UserProfileActivity).loadUserPicture(
                mUserDetails.image,
                binding.ivUserPhoto
            )
            binding.etFirstName.isEnabled = true
            binding.etLastName.isEnabled = true
            binding.etEmail.isEnabled = false


            if (mUserDetails.mobile != 0L) {
                binding.etMobileNumber.setText(mUserDetails.mobile.toString())
            }
            if (mUserDetails.gender == Constants.MALE) {
                binding.rbMale.isChecked = true
            } else binding.rbFemale.isChecked = true


        }

        // adding set on click listener for profile image
        binding.ivUserPhoto.setOnClickListener(this@UserProfileActivity)
        binding.btnSubmit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {

                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check READ_EXTERNAL_STORAGE permission and if it is not allowed we send a request.
                    if (ContextCompat.checkSelfPermission(
                            this, Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChooser(this)
                    } else {

                        /* Requests permissions to be granted to this application. These permissions
                        must be requested in your manifest, they should not be granted to your app,
                        and they should have protection level.
                         */

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit -> {

                    if (validateUserProfileDetails()) {

                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageUri != null) {
                            FirestoreClass().uploadImageToCloudStorage(
                                this@UserProfileActivity, mSelectedImageUri
                            )
                        } else {
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails() {

        val userHashMap = HashMap<String, Any>()

        val firstName = binding.etFirstName.text.toString().trim { it <= ' ' }
        if (firstName != mUserDetails.firstName) {userHashMap[Constants.FIRST_NAME] = firstName}

        val lastName = binding.etLastName.text.toString().trim { it <= ' ' }
        if (lastName != mUserDetails.lastName) {userHashMap[Constants.LAST_NAME] = lastName}


        val gender = if (binding.rbMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }

        if (mUserProfileImageURL.isNotEmpty()) {
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        val mobileNumber = binding.etMobileNumber.text.toString().trim { it <= ' ' }
        if (mobileNumber.isNotEmpty() && mobileNumber != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        if (gender.isNotEmpty() && gender != mUserDetails.gender) {userHashMap[Constants.GENDER] = gender}
        // key: gender value: value
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            // If permission is granted.

            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this)
            } else {
                // Displaying another toast if permission is not granted.
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {

                        // The uri of selected image from phone storage
                        mSelectedImageUri = data.data!!
                        GlideLoader(this).loadUserPicture(mSelectedImageUri!!, binding.ivUserPhoto)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etMobileNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}