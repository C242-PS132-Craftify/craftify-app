package com.craftify.craftify_app.ui.blog

import GetAllBlogResponse
import GetAllBlogResponseItem
import androidx.lifecycle.*
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.repository.BlogRepository
import kotlinx.coroutines.launch

class BlogViewModel(private val repository: BlogRepository) : ViewModel() {

    private val _blogs = MutableLiveData<Result<GetAllBlogResponse>>()
    val blogs: LiveData<Result<GetAllBlogResponse>> get() = _blogs

    private val _blogDetails = MutableLiveData<Result<GetAllBlogResponseItem>>()
    val blogDetails: LiveData<Result<GetAllBlogResponseItem>> get() = _blogDetails

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
}