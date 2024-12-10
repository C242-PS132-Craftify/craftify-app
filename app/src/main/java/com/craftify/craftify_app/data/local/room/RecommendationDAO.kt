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
    fun getSavedCraft(): LiveData<List<RecommendationEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCraft(craft: RecommendationEntity)

    @Update
    fun updateCraft(craft : RecommendationEntity)

    @Query("DELETE FROM saved_craft")
    fun deleteAll()

    @Query("DELETE FROM saved_craft WHERE title = :title")
    fun deleteCraft(title : String)

    @Query("SELECT EXISTS(SELECT * FROM saved_craft WHERE title = :title)")
    fun isCraftSaved(title: String): Boolean
}