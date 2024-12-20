package com.craftify.craftify_app.ui.result

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.repository.RecommendationRepository
import kotlinx.coroutines.launch

class ResultViewModel(private val repo : RecommendationRepository) : ViewModel() {
    val getSavedCraft: LiveData<List<RecommendationEntity>> = repo.getFavoriteCraft()

    /*
    fun saveCraft(title: String, imageUrl: String) {
        viewModelScope.launch {
            val craft = RecommendationEntity(title = title, image = imageUrl)
            repo.saveCraft(craft)
            Log.d("DatabaseCheck", "Insertion Success : $craft")
        }
    }
    */
}