package com.craftify.craftify_app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.UserRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val authRepository: AuthRepository, userRepository: UserRepository) :ViewModel() {
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User?> = _currentUser

    init {
        viewModelScope.launch{
            val user = authRepository.getCurrentUser()
            _currentUser.postValue(userRepository.getUser(user?.uid.toString()))
        }
    }

    fun logout(){
        authRepository.logout()
    }
}