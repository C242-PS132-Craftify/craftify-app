package com.craftify.craftify_app.data.repository

import androidx.lifecycle.LiveData
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.local.room.RecommendationDAO
import com.google.firebase.firestore.FirebaseFirestore

class SavedCraftRepository private constructor(
    private val craftDao: RecommendationDAO,
){

    fun getAllCraft() : LiveData<List<RecommendationEntity>> {
        return craftDao.getSavedCraft()
    }

    suspend fun saveCraft(craft : RecommendationEntity){
        craftDao.insertCraft(craft)
    }

    suspend fun deleteCraft(craft : RecommendationEntity){
        craftDao.deleteCraft(craft.title)
    }

    companion object {
        @Volatile
        private var instance: SavedCraftRepository? = null
        fun getInstance(
            craftDao: RecommendationDAO
        ): SavedCraftRepository =
            instance ?: synchronized(this) {
                instance ?: SavedCraftRepository(craftDao)
            }.also { instance = it }
    }
}