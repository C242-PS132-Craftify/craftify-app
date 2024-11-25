package com.craftify.craftify_app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.model.RegistrationResult
import com.craftify.craftify_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _registrationState = MutableLiveData<RegistrationResult>()
    val registrationState : LiveData<RegistrationResult> get() = _registrationState

    fun registerUser(email : String, username : String, password : String){
        viewModelScope.launch{
            val result = userRepository.registerUser(email, username, password)
            if (result.isSuccess){
                _registrationState.value = RegistrationResult(success = true)
            } else {
                _registrationState.value = RegistrationResult(error = result.exceptionOrNull()?.message)
            }
        }
    }
}