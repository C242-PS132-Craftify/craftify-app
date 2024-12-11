package com.craftify.craftify_app.ui.craft

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.repository.RecommendationRepository
import kotlinx.coroutines.launch

class DetailCraftViewModel (private val repository: RecommendationRepository) : ViewModel() {

    fun getCraftByName(projectName: String): LiveData<Result<RecommendationEntity>> {
        return repository.getCraftByName(projectName)
    }

    fun toggleFavorite(projectName: String, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(projectName, isFavorite)
        }
    }
}