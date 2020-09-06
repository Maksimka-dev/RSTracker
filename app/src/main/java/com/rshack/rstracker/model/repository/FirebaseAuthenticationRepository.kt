package com.rshack.rstracker.model.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.rshack.rstracker.utils.Result
import com.rshack.rstracker.utils.TAG
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationRepository @Inject constructor() : IAuthenticationRepository {

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun getCurrentUserEmail(): String? = firebaseAuth.currentUser?.email

    override fun getAuth(): FirebaseAuth = firebaseAuth

    override suspend fun login(email: String, password: String): Result<String?> =
        try {
            val user = firebaseAuth.signInWithEmailAndPassword(email, password).await().user
            if (user != null) {
                Log.d(TAG, "signInWithEmail:success")
                Result.Success(user.uid)
            } else {
                Log.d(TAG, "signInWithEmail:failure")
                Result.Success(null)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.stackTraceToString())
            Result.Error(e)
        }

    override suspend fun register(email: String, password: String): Result<String?> =
        try {
            val user = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user
            if (user != null) {
                Log.d(TAG, "createUserWithEmail:success")
                Result.Success(user.uid)
            } else {
                Log.d(TAG, "createUserWithEmail:failure")
                Result.Success(null)
            }
        } catch (e: Exception) {
            Log.d(TAG, e.stackTraceToString())
            Result.Error(e)
        }

    override fun logout() {
        try {
            firebaseAuth.signOut()
            Log.d(TAG, "firebaseAuthLogout:success")
        } catch (e: Exception) {
            Log.d(TAG, "firebaseAuthLogout:failure $e")
        }
    }
}
