package com.craftify.craftify_app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.craftify.craftify_app.data.local.entity.RecommendationEntity


@Database(entities = [RecommendationEntity::class], version = 2, exportSchema = false)
abstract class RecommendationDatabase : RoomDatabase() {
    abstract fun recommendationDAO(): RecommendationDAO

    companion object {
        @Volatile
        private var instance: RecommendationDatabase? = null
        fun getInstance(context: Context): RecommendationDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecommendationDatabase::class.java, "recommendation.db"
                ).build()
            }
    }
}