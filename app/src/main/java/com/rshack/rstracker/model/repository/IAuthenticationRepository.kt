package com.rshack.rstracker.model.repository

import com.google.firebase.auth.FirebaseAuth
import com.rshack.rstracker.utils.Result

interface IAuthenticationRepository {
    fun getCurrentUserEmail(): String?
    fun getAuth(): FirebaseAuth
    suspend fun login(email: String, password: String): Result<String?>
    suspend fun register(email: String, password: String): Result<String?>
    fun logout()
}
