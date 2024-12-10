package com.craftify.craftify_app.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_info")

    fun getOnboardingInfo(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }
    }

    suspend fun saveOnboardingInfo(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[ONBOARDING_KEY] = isLogin
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: SettingPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}