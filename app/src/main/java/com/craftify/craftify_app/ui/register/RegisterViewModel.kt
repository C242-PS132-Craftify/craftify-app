package com.craftify.craftify_app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<FirebaseUser>>()
    val registerResult: LiveData<Result<FirebaseUser>> = _registerResult

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.register(username, email, password)
            _registerResult.postValue(result)
        }
    }
}