package com.craftify.craftify_app.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.model.User
import com.craftify.craftify_app.data.repository.SavedCraftRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val repo : SavedCraftRepository) : ViewModel() {
    val savedCraft: LiveData<List<RecommendationEntity>> = repo.getAllCraft()

    fun saveCraft(title: String, imageUrl: String) {
        viewModelScope.launch {
            val craft = RecommendationEntity(title = title, image = imageUrl)
            repo.saveCraft(craft)
            Log.d("DatabaseCheck", "Insertion Success : $craft")
        }
    }



}