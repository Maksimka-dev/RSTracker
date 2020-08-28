package com.rshack.rstracker.model.repository

import com.rshack.rstracker.Result

interface IAuthenticationRepository {
    fun isLoggedIn(): Boolean
    suspend fun login(email: String, password: String): Result<String?>
    suspend fun register(email: String, password: String): Result<String?>
    fun logout()
}