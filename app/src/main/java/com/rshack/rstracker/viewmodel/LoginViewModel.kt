package com.rshack.rstracker.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rshack.rstracker.model.repository.IAuthenticationRepository
import com.rshack.rstracker.utils.AuthUiState
import com.rshack.rstracker.utils.Result
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val firebaseAuthenticationRepository: IAuthenticationRepository
) : ViewModel() {

    private val _authResult = MutableLiveData<AuthUiState>()
    val authResult: LiveData<AuthUiState>
        get() = _authResult

    fun login(email: String, password: String) = viewModelScope.launch {
        _authResult.value = AuthUiState.Loading
        when (firebaseAuthenticationRepository.login(email, password)) {
            is Result.Success -> _authResult.value = AuthUiState.Success
            is Result.Error -> _authResult.value = AuthUiState.Error
        }
    }

    fun clearAuthResult() {
        _authResult.value = null
    }

    fun getFirebaseAuth() = firebaseAuthenticationRepository.getAuth()
}
