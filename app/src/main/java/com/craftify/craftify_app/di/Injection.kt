package com.craftify.craftify_app.di

import android.content.Context
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object Injection {
    fun provideAuthRepository(context : Context) : AuthRepository {
        val auth = FirebaseAuth.getInstance()
        val firestore  = FirebaseFirestore.getInstance()
        return AuthRepository(auth, firestore)
    }

    fun provideUserRepository(context: Context) : UserRepository {
        val firestore = FirebaseFirestore.getInstance()
        return  UserRepository(firestore)
    }
}