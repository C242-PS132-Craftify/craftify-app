package com.craftify.craftify_app.ui.myblog

import GetAllBlogResponse
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.BlogRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class MyBlogViewModel (private val repository: BlogRepository, private val authRepository: AuthRepository) : ViewModel() {
    private val _blogs = MutableLiveData<Result<GetAllBlogResponse>>()
    val blogs : LiveData<Result<GetAllBlogResponse>> get() = _blogs

    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> = _currentUser

    init {
        _currentUser.postValue(authRepository.getCurrentUser())
    }

    fun fetchMyBlogs() {
        viewModelScope.launch {
            val userId = _currentUser.value?.uid
            _blogs.value = Result.Loading
            if (userId != null) {
                _blogs.value = repository.getUserBlogs(userId)
            }
        }
    }

    fun deleteBlog(id : String) {
        viewModelScope.launch {
            _blogs.value = Result.Loading
            val result = repository.deleteBlog(id)
            if (result is Result.Success) {
                fetchMyBlogs()
            }
        }
    }
}