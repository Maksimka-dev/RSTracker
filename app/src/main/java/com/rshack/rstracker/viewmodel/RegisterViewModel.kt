package com.rshack.rstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rshack.rstracker.AuthUiState
import com.rshack.rstracker.Result
import com.rshack.rstracker.model.repository.FirebaseAuthenticationRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val firebaseAuthenticationRepository = FirebaseAuthenticationRepository()

    private val _authResult = MutableLiveData<AuthUiState>()
    val authResult: LiveData<AuthUiState>
        get() = _authResult

    fun register(email: String, password: String) = viewModelScope.launch {
        _authResult.value = AuthUiState.Loading
        when (firebaseAuthenticationRepository.register(email, password)) {
            is Result.Success -> _authResult.value = AuthUiState.Success
            is Result.Error -> _authResult.value = AuthUiState.Error
        }
    }
}