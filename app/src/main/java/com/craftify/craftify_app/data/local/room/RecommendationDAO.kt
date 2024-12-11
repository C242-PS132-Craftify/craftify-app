package com.craftify.craftify_app.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.craftify.craftify_app.data.local.entity.RecommendationEntity

@Dao
interface RecommendationDAO {
    @Query("SELECT * FROM saved_craft")
    fun getAll(): LiveData<List<RecommendationEntity>>

    @Query("SELECT * FROM saved_craft where is_favorite=1")
    fun getAllFavorites() : LiveData<List<RecommendationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCraft(craft: RecommendationEntity)

    @Update
    fun updateCraft(craft : RecommendationEntity)

    @Query("UPDATE saved_craft SET is_favorite = :isFavorite WHERE project_name = :projectName")
    suspend fun updateFavoriteStatus(projectName:  String, isFavorite: Boolean)

    @Query("delete from saved_craft where is_favorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM saved_craft WHERE project_name = :name AND is_favorite = 1)")
    suspend fun isEventFavorite(name : String):Boolean

    @Query("SELECT is_favorite FROM  saved_craft WHERE id = :id")
    suspend fun isSpecificEventFavorite(id: String): Boolean

    @Query("SELECT * FROM saved_craft WHERE project_name = :projectName")
    fun getByName(projectName: String): RecommendationEntity
}