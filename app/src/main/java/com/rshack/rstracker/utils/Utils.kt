package com.rshack.rstracker.utils

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.rshack.rstracker.R

const val TAG = "my_tag"

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

fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
            )
            .into(imgView)
    }
}
