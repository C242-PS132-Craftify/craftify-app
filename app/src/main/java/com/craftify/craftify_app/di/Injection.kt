package com.craftify.craftify_app.di

import android.content.Context
import com.craftify.craftify_app.data.local.SettingPreferences
import com.craftify.craftify_app.data.local.dataStore as onboarding
import com.craftify.craftify_app.data.local.room.RecommendationDatabase
import com.craftify.craftify_app.data.local.preferences.SettingsPreferences
import com.craftify.craftify_app.data.local.preferences.dataStore
import com.craftify.craftify_app.data.repository.AuthRepository
import com.craftify.craftify_app.data.repository.BlogRepository
import com.craftify.craftify_app.data.repository.RecommendationRepository
import com.craftify.craftify_app.data.repository.UserRepository
import com.craftify.craftify_app.data.server.api.ApiConfig
import com.craftify.craftify_app.data.server.api.cc.CCAPIService
import com.craftify.craftify_app.data.server.api.cc.CCApiConfig
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
        val apiService = CCApiConfig.retrofit.create(CCAPIService::class.java)
        return  UserRepository(firestore, apiService)
    }

    fun provideBlogRepository(context: Context) : BlogRepository {
        val apiService = CCApiConfig.retrofit.create(CCAPIService::class.java)
        return BlogRepository(apiService)
    }

    fun provideRecommendationRepository(context: Context): RecommendationRepository {
        val apiService = ApiConfig.getApiService()
        val database = RecommendationDatabase.getInstance(context)
        val dao = database.recommendationDAO()
        return RecommendationRepository.getInstance(dao, apiService)
    }

    fun provideOnboardingRepository(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.onboarding)
    }
    fun provideSettingsPreferences(context: Context) : SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }
}