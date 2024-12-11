package com.craftify.craftify_app.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.craftify.craftify_app.di.Injection
import com.craftify.craftify_app.ui.blog.BlogViewModel
import com.craftify.craftify_app.ui.craft.DetailCraftViewModel
import com.craftify.craftify_app.ui.login.LoginViewModel
import com.craftify.craftify_app.ui.myblog.MyBlogViewModel
import com.craftify.craftify_app.ui.onboarding.OnboardingViewModel
import com.craftify.craftify_app.ui.profile.ProfileViewModel
import com.craftify.craftify_app.ui.register.RegisterViewModel
import com.craftify.craftify_app.ui.result.ResultViewModel
import com.craftify.craftify_app.ui.settings.SettingsViewModel

class ViewModelFactory(
    private val context: Context,
) : ViewModelProvider.NewInstanceFactory() {
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
                ProfileViewModel(
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(BlogViewModel::class.java) -> {
                BlogViewModel(
                    Injection.provideBlogRepository(context),
                    Injection.provideAuthRepository(context),
                    Injection.provideUserRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(MyBlogViewModel::class.java) -> {
                MyBlogViewModel(
                    Injection.provideBlogRepository(context),
                    Injection.provideAuthRepository(context)
                ) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(Injection.provideSettingsPreferences(context)) as T
            }
            modelClass.isAssignableFrom(ResultViewModel::class.java) -> {
                ResultViewModel(Injection.provideRecommendationRepository(context)) as T
            }
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> {
                OnboardingViewModel(Injection.provideOnboardingRepository(context)) as T
            }
            modelClass.isAssignableFrom(DetailCraftViewModel::class.java) -> {
                DetailCraftViewModel(Injection.provideRecommendationRepository(context)) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
