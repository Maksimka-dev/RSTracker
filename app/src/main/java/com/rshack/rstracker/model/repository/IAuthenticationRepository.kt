package com.rshack.rstracker.model.repository

import com.rshack.rstracker.utils.Result

interface IAuthenticationRepository {
    fun getCurrentUserEmail(): String?
    suspend fun login(email: String, password: String): Result<String?>
    suspend fun register(email: String, password: String): Result<String?>
    fun logout()
}
