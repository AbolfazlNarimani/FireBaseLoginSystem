package com.example.foroshgah_slami.ui.UI.activities

import android.R
import android.app.Dialog
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    private lateinit var mProgressDialog: Dialog
    private var doubleBackToExitPressedOnce = false

    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    com.example.foroshgah_slami.R.color.colorSnackBarError
                )
            )
        } else {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    com.example.foroshgah_slami.R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {
        mProgressDialog = Dialog(this)

        /*Set the screen content from a layout resource
        the resource will be inflated, adding all top-level views to the screen
         */
        mProgressDialog.setContentView(com.example.foroshgah_slami.R.layout.dialog_progress)

        mProgressDialog.findViewById<TextView>(com.example.foroshgah_slami.R.id.tv_progress_text).text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen
        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true

        Toast.makeText(
            this@BaseActivity,
            resources.getString(com.example.foroshgah_slami.R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT
        ).show()

        android.os.Handler().postDelayed({ doubleBackToExitPressedOnce = false}, 2000)
    }
}