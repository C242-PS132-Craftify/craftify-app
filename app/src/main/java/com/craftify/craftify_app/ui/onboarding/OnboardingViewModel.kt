package com.craftify.craftify_app.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.local.SettingPreferences
import kotlinx.coroutines.launch

class OnboardingViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getOnboardingInfo().asLiveData()
    }

    fun saveThemeSetting(onboarding: Boolean) {
        viewModelScope.launch {
            pref.saveOnboardingInfo(onboarding)
        }
    }
}