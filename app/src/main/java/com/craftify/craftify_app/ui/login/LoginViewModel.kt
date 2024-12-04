package com.craftify.craftify_app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.local.SettingPreferences
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getLoginInfo(): LiveData<Boolean> {
        return pref.getLoginInfo().asLiveData()
    }

    fun setLoginInfo(isLogin: Boolean) {
        viewModelScope.launch {
            pref.saveLoginInfo(isLogin)
        }
    }

}