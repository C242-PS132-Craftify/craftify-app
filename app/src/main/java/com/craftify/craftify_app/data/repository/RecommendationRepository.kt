package com.craftify.craftify_app.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.local.room.RecommendationDAO
import com.craftify.craftify_app.data.server.api.ApiService
import com.craftify.craftify_app.data.server.response.DataItem
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.utils.toRecommendationEntity
import kotlinx.coroutines.Dispatchers

class RecommendationRepository private constructor(
    private val craftDao: RecommendationDAO,
    private val apiService: ApiService
){
    fun getCraftByName(projectName: String) : LiveData<Result<RecommendationEntity>> = liveData(Dispatchers.IO){
        emit(Result.Loading)
        try {
            val localCraft = craftDao.getByName(projectName)
            if (localCraft != null){
                emit(Result.Success(localCraft))
            } else {
                val response = apiService.getProject(projectName).execute()
                if (response.isSuccessful){
                    val data = response.body()
                    val craft = data?.data?.filterNotNull()?.map { it.toRecommendationEntity() }
                    craftDao
                    craft?.forEach{ el->
                        craftDao.insertCraft(el)
                        emit(Result.Success(el))
                    } ?: emit(Result.Error("Recipe not found"))
                } else {
                    emit(Result.Error("Recipe not found"))
                }
            }
        } catch (e : Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteCraft():LiveData<List<RecommendationEntity>>{
        return craftDao.getAllFavorites()
    }

    suspend fun toggleFavorite(projectName : String, isFavorite : Boolean){
        craftDao.updateFavoriteStatus(projectName,isFavorite)
    }

    suspend fun clearDB(){
        craftDao.deleteAll()
    }

    companion object {
        @Volatile
        private var instance: RecommendationRepository? = null
        fun getInstance(
            craftDao: RecommendationDAO,
            apiService: ApiService
        ): RecommendationRepository =
            instance ?: synchronized(this) {
                instance ?: RecommendationRepository(craftDao, apiService)
            }.also { instance = it }
    }
}