package com.popcorn.fire

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import com.popcorn.MainActivity
import javax.inject.Inject

class AuthProcess @Inject constructor(
    private val auth: FirebaseAuth,
    private val context: Context
) {
    fun signUp(email: String, password: String, username: String, onComplete: (Boolean,String?) -> Unit) {
        var user: FirebaseUser?
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                    user = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = username
                    }
                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Log.d("firebase_tag", "User profile updated.")
                            }
                        }
                } else {
                    onComplete(false, task.exception?.message)
                    Toast.makeText(
                        context,
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(MainActivity()) { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
                    Toast.makeText(
                        context,
                        "${task.exception?.message}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}