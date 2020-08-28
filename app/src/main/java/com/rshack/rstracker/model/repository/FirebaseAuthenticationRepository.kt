package com.rshack.rstracker.model.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.rshack.rstracker.Result
import com.rshack.rstracker.TAG
import kotlinx.coroutines.tasks.await

class FirebaseAuthenticationRepository : IAuthenticationRepository {

    private val firebaseAuth by lazy {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseAuth.getInstance()
    }

    override fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null

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
            Log.d(TAG, "firebaseAuthLogout:failure")
        }
    }

//    override fun logout(): Result<Boolean> =
//        try {
//            firebaseAuth.signOut()
//            Result.Success(true)
//        } catch (e: Exception) {
//            Result.Error(e)
//        }

}