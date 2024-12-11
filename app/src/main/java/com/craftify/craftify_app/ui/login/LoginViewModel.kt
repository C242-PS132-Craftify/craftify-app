package com.craftify.craftify_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _loginResult= MutableLiveData<Result<FirebaseUser>>()
    val loginResult: LiveData<Result<FirebaseUser>> = _loginResult

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    init {
        _currentUser.postValue(authRepository.getCurrentUser())
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.postValue(Result.Loading)
            val result = authRepository.login(email, password)
            _loginResult.postValue(result)
            if (result is Result.Success) {
                _currentUser.value = result.data
            }
        }
    }
}