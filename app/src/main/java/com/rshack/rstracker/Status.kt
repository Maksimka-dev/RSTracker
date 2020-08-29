package com.rshack.rstracker

const val TAG = "my_tag"

enum class Status {
    OK,
    Error,
    FBError
}

sealed class Result<out T> {
    class Success<out T>(val value: T) : Result<T>()
    class Error(val exception: Throwable) : Result<Nothing>()
}

sealed class AuthUiState {
    object Loading : AuthUiState()
    object Success : AuthUiState()
    object Error : AuthUiState()
}

//        firebaseAuth.currentUser?.reload()?.addOnFailureListener {
//            if (it is FirebaseAuthInvalidUserException) {
//                startActivity(Intent(applicationContext, LoginActivity::class.java))
//                finish()
//            }
//        }
