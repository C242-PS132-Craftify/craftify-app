package com.craftify.craftify_app.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.RecommendationRepository
import com.craftify.craftify_app.data.repository.UserRepository
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(private val authRepository: AuthRepository, private val userRepository: UserRepository, private val recommendationRepository: RecommendationRepository) :ViewModel() {
    private val _currentUser = MutableLiveData<Result<User>>()
    val currentUser: LiveData<Result<User>> = _currentUser

    private val _uploadImage = MutableLiveData<Result<UploadHeaderImageResponse>>()
    val uploadImage: LiveData<Result<UploadHeaderImageResponse>> get() = _uploadImage

    private val _updateProfilePicture = MutableLiveData<Result<User>>()
    val updateProfilePicture: LiveData<Result<User>> get() = _updateProfilePicture

    fun getUser() {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            val user = authRepository.getCurrentUser()
            val result = userRepository.getUser(user?.uid.toString())
            _currentUser.postValue(result)
        }
    }

    fun uploadAndSetProfilePicture(image: MultipartBody.Part) {
        viewModelScope.launch {
            _uploadImage.value = Result.Loading

            val uploadResult = userRepository.uploadImage(image)
            _uploadImage.postValue(uploadResult)

            if (uploadResult is Result.Success) {
                val uploadedImageUrl = uploadResult.data.url
                val user = authRepository.getCurrentUser()
                if (user != null) {
                    _updateProfilePicture.value = userRepository.addPhotoProfile(uploadedImageUrl!!, user.uid)
                } else {
                    _updateProfilePicture.value = Result.Error("User is not logged in")
                }
            }
        }
    }



    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
            recommendationRepository.clearDB()
        }
    }
}