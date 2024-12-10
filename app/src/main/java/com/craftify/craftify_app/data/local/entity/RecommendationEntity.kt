package com.craftify.craftify_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_craft")
data class RecommendationEntity(
    //@PrimaryKey(autoGenerate = true) val id: Int,

    @field:ColumnInfo(name = "title")
    @PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "image")
    var image: String

)