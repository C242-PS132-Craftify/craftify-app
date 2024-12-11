package com.craftify.craftify_app.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.utils.DataConverters


@Database(entities = [RecommendationEntity::class], version = 3, exportSchema = false)
@TypeConverters(DataConverters::class)
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