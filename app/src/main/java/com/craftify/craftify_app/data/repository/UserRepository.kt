package com.craftify.craftify_app.data.repository

import com.craftify.craftify_app.data.model.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth = FirebaseAuth.getInstance()

    suspend fun registerUser(email : String, username : String, password : String) : Result<User>{
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid?: throw Exception("User ID is null")

            val user = User(id = userId, email = email, username = username)

            Result.success(user)
        } catch (e : Exception){
            Result.failure(e)
        }
    }
}