package com.overshoot.data.datasource.remote.authentication

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthenticationServiceImpl: AuthenticationService {

    private var auth: FirebaseAuth = Firebase.auth

    override fun getUserInfo(): FirebaseUser? {
        return auth.currentUser
    }

    override fun loginWithEmail(
        email: String,
        password: String,
        onSuccess: (Task<AuthResult>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(it)
                }
                else {
                    it.exception?.also { exception ->
                        onFailure(exception)
                    }
                }
            }
    }

    override fun registerWithEmail(
        email: String,
        password: String,
        onSuccess: (Task<AuthResult>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess(it)
                }
                else {
                    it.exception?.also { exception ->
                        onFailure(exception)
                    }
                }
            }
    }

    override fun logout() {
        auth.signOut()
    }

}