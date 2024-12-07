package com.craftify.craftify_app.data.repository

import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(private val db : FirebaseFirestore)  {
    suspend fun getUser (uid : String) : User {
        return try {
            val documentSnapshot = db.collection("users").document(uid).get().await()
            if (documentSnapshot.exists()) {
                val user = documentSnapshot.data ?: throw Exception("User data is empty")
                val userData = User(
                    username = user["username"] as? String,
                    email = user["email"] as? String,
                    profile_picture = user["profile_picture"] as? String
                )
                userData
            } else {
                throw Exception("User does not exists")
            }
        } catch (e : Exception) {
            throw Exception(e.message)
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            db: FirebaseFirestore
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(db)
            }.also { instance = it }
    }
}