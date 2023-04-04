package com.example.foroshgah_slami.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.foroshgah_slami.activities.LoginActivity
import com.example.foroshgah_slami.activities.RegisterActivity
import com.example.foroshgah_slami.models.User
import com.example.foroshgah_slami.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass  {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {

        // the "users" is a collection name. if the collection is already created then it will not create the same collection.
        mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are field and the SetOption is set to merge. It is for if we wants to marge later on instead of replacing the fields.
            .set(userInfo , SetOptions.merge())

            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.userRegistrationSuccess()
        }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        // Here we pass the collection name from witch we wants the data.
        mFireStore.collection(Constants.USERS)
            // The document id to get the Fields of user.
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                // here we have received the document snapshots which is converted into the User Data model object.
                val user = document.toObject(User::class.java)!!


                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYSHOPPAL_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // key: logged_in_username / value:  Frank Tank

                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firsName} ${user.lastName}"
                )
                editor.apply()

                // START
                when(activity) {
                    is LoginActivity -> {
                        // call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                }
                // END
            }
    }

}