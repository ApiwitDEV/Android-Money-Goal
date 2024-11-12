package com.overshoot.data.datasource.remote.model.authentication

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.overshoot.data.datasource.Failure
import com.overshoot.data.datasource.ResultData
import com.overshoot.data.datasource.Success
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthenticationServiceImpl: AuthenticationService {

    private var auth: FirebaseAuth = Firebase.auth

    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? =  null

    private val timeOut: Long = 60

//    override fun getAccessToken() = callbackFlow<ResultData<String>> {
//        auth.currentUser?.getIdToken(true)
//            ?.addOnSuccessListener {
//                trySend(Success(it.token?:""))
//            }
//            ?.addOnFailureListener {
//                trySend(Failure(message = it.toString()))
//            }
//        awaitClose {
//            cancel()
//        }
//    }

    override suspend fun getAccessToken(): String? {
        return auth.currentUser?.getIdToken(true)?.await()?.token
    }

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
//                    saveToken()
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

    override fun sendVerificationCode(
        phoneNumber: String,
        onSuccess: (Long) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                onSuccess(timeOut)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                onFailure(e)
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                onSuccess(timeOut)
                // Save verification ID and resending token so we can use them later
                this@AuthenticationServiceImpl.verificationId = verificationId
                resendToken = token
            }

            override fun onCodeAutoRetrievalTimeOut(e: String) {
//                onFailure(Exception(message = e))
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(timeOut, TimeUnit.SECONDS) // Timeout and unit
//            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun signInWithPhoneNumber(code: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    onSuccess()

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    task.exception?.also(onFailure)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        task.exception?.also(onFailure)
                    }
                    // Update UI
                }
            }
    }

}