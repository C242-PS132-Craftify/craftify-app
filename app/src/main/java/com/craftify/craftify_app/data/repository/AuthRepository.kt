package com.craftify.craftify_app.data.repository

import com.craftify.craftify_app.data.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository (private val auth : FirebaseAuth, private val db : FirebaseFirestore) {
    suspend fun register(username : String, email : String, password : String) : Result<FirebaseUser> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw  Exception ("User not found.")

            val userId = user.uid

            val userData = hashMapOf(
                "username" to username,
                "email" to email,
                "profile_picture" to null
            )

            db.collection("users").document(userId).set(userData).await()
            Result.Success(user)
        } catch (e : Exception){
            Result.Error(e.message.toString())
        }
    }

    suspend fun login(email : String, password: String) : Result<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw  Exception ("User not found.")

            Result.Success(user)
        } catch (e : Exception){
            Result.Error(e.message.toString())
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun logout() {
        auth.signOut()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}