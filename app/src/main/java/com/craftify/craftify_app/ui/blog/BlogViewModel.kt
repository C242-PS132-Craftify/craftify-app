package com.craftify.craftify_app.ui.blog

import GetAllBlogResponse
import GetAllBlogResponseItem
import androidx.lifecycle.*
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.BlogRepository
import com.craftify.craftify_app.data.repository.UserRepository
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class BlogViewModel(private val repository: BlogRepository, private val authRepository: AuthRepository, private val userRepository: UserRepository) : ViewModel() {

    private val _blogs = MutableLiveData<Result<GetAllBlogResponse>>()
    val blogs: LiveData<Result<GetAllBlogResponse>> get() = _blogs

    private val _blogDetails = MutableLiveData<Result<GetAllBlogResponseItem>>()
    val blogDetails: LiveData<Result<GetAllBlogResponseItem>> get() = _blogDetails

    private val _addBlog = MutableLiveData<Result<GetAllBlogResponseItem>>()
    val addBlog: LiveData<Result<GetAllBlogResponseItem>> get() = _addBlog

    private val _editBlog = MutableLiveData<Result<GetAllBlogResponseItem>>()
    val editBlog: LiveData<Result<GetAllBlogResponseItem>> get() = _editBlog

    private val _deleteBlog = MutableLiveData<Result<Boolean>>()
    val deleteBlog: LiveData<Result<Boolean>> get() = _deleteBlog

    private val _uploadImage = MutableLiveData<Result<UploadHeaderImageResponse>>()
    val uploadImage: LiveData<Result<UploadHeaderImageResponse>> get() = _uploadImage

    private val _currentUser = MutableLiveData<Result<User>>()
    val currentUser: LiveData<Result<User>> = _currentUser

    private val _currentUserId = MutableLiveData<String>()
    val currentUserId : LiveData<String?> = _currentUserId

    init {
        viewModelScope.launch {
            val user = authRepository.getCurrentUser()
            _currentUserId.postValue(user?.uid.toString())
            _currentUser.postValue(userRepository.getUser(user?.uid.toString()))
        }
    }
    fun fetchAllBlogs() {
        viewModelScope.launch {
            _blogs.value = Result.Loading
            _blogs.value = repository.getAllBlogs()
        }
    }

    fun fetchBlogDetails(id: String) {
        viewModelScope.launch {
            _blogDetails.value = Result.Loading
            _blogDetails.value = repository.getBlogDetails(id)
        }
    }

    fun addBlog(title: String, author: String, userId : String, content: String, headerImage: String) {
        viewModelScope.launch {
            _addBlog.value = Result.Loading
            _addBlog.value = repository.addBlog(title, author, userId, content, headerImage)
        }
    }

    fun editBlog(id: String, title: String, author: String, userId: String, content: String, headerImage: String) {
        viewModelScope.launch {
            _editBlog.value = Result.Loading
            _editBlog.value = repository.editBlog(id, title, author, userId, content, headerImage)
        }
    }

    fun deleteBlog(id: String) {
        viewModelScope.launch {
            _deleteBlog.value = Result.Loading
            _deleteBlog.value = repository.deleteBlog(id).let { result ->
                if (result is Result.Success) Result.Success(true) else Result.Success(false)
            }
        }
    }

    fun uploadBlogImage(image: MultipartBody.Part) {
        viewModelScope.launch {
            _uploadImage.value = Result.Loading
            _uploadImage.value = repository.uploadBlogImage(image)
        }
    }
}
