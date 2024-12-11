package com.craftify.craftify_app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_craft")
data class RecommendationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "project_img")
    val projectImg: String? = null,

    @ColumnInfo(name = "project_materials")
    val projectMaterials: List<String?>? = null,

    @ColumnInfo(name = "project_name")
    val projectName: String? = null,

    @ColumnInfo(name = "project_recipe")
    val projectRecipe: List<String?>? = null,

    @ColumnInfo(name = "is_favorite")
    val isFavorite : Boolean
)