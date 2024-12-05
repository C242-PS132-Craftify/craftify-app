package com.craftify.craftify_app.data.repository

import GetAllBlogResponse
import GetAllBlogResponseItem
import com.craftify.craftify_app.data.server.api.cc.CCAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.server.api.cc.response.DeleteBlogResponse
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import okhttp3.MultipartBody

class BlogRepository (private val apiService : CCAPIService) {
    suspend fun getAllBlogs(): Result<GetAllBlogResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllBLog().execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun getBlogDetails(id: String): Result<GetAllBlogResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getBlogDetails(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun uploadBlogImage(image: MultipartBody.Part): Result<UploadHeaderImageResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.uploadBlogImage(image).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun addBlog(
        title: String,
        author: String,
        content: String,
        headerImage: String
    ): Result<GetAllBlogResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.addBlog(title, author, content, headerImage).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun editBlog(
        id: String,
        title: String,
        author: String,
        content: String,
        headerImage: String
    ): Result<GetAllBlogResponseItem> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.editBlog(id, title, author, content, headerImage).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    suspend fun deleteBlog(id: String): Result<DeleteBlogResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteBlog(id).execute()
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response")
                } else {
                    Result.Error("Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "Unknown error")
            }
        }
    }

    companion object {
        @Volatile
        private var instance: BlogRepository? = null
        fun getInstance(
            apiService: CCAPIService
        ): BlogRepository =
            instance ?: synchronized(this) {
                instance ?: BlogRepository(apiService)
            }.also { instance = it }
    }
}