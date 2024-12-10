package com.craftify.craftify_app.data.repository

import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.server.api.cc.CCAPIService
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class UserRepository(private val db : FirebaseFirestore, private val apiService : CCAPIService)  {
    suspend fun getUser (uid : String) : Result<User> {
        return try {
            val documentSnapshot = db.collection("users").document(uid).get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.data ?: throw Exception("User data is empty")
                val userData = User(
                    username = user["username"] as? String,
                    email = user["email"] as? String,
                    profile_picture = user["profile_picture"] as? String
                )
                Result.Success(userData)
            } else {
                throw Exception("User does not exists")
            }
        } catch (e : Exception) {
            Result.Error(e.message.toString())
        }
    }

    suspend fun uploadImage(image: MultipartBody.Part): Result<UploadHeaderImageResponse> {
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

    suspend fun addPhotoProfile(imageUrl : String, userId : String) : Result<User> {
        return try {
            db.collection("users").document(userId)
                .update("profile_picture", imageUrl)
                .await()

            val updatedUser = getUser(userId)
            when (updatedUser) {
                is Result.Success -> updatedUser
                is Result.Error -> throw Exception("Failed updating user")
                Result.Loading -> {
                    Result.Loading
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Failed to update profile picture")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            db: FirebaseFirestore,
            apiService: CCAPIService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(db,apiService)
            }.also { instance = it }
    }
}