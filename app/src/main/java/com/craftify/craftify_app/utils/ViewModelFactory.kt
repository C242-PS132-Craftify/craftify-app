package com.craftify.craftify_app.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.craftify.craftify_app.di.Injection
import com.craftify.craftify_app.ui.blog.BlogViewModel
import com.craftify.craftify_app.ui.login.LoginViewModel
import com.craftify.craftify_app.ui.profile.ProfileViewModel
import com.craftify.craftify_app.ui.register.RegisterViewModel

class ViewModelFactory(
    private val context : Context,
) : ViewModelProvider.NewInstanceFactory(){
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(Injection.provideAuthRepository(context)) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(Injection.provideAuthRepository(context), Injection.provideUserRepository(context)) as T
            }
            modelClass.isAssignableFrom(BlogViewModel::class.java) -> {
                BlogViewModel(Injection.provideBlogRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}