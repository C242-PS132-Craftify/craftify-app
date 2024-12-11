package com.craftify.craftify_app.utils

import androidx.room.TypeConverter
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.server.response.DataItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromStringList(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<String?>?): String? {
        return gson.toJson(list)
    }
}