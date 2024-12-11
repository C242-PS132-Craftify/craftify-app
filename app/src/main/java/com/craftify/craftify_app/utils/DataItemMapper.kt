package com.craftify.craftify_app.utils

import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.server.response.DataItem

fun DataItem.toRecommendationEntity(isFavorite : Boolean = false) : RecommendationEntity {
    return RecommendationEntity(
        projectImg = this.projectImg,
        projectMaterials = this.projectMaterials,
        projectName = this.projectName,
        projectRecipe = this.projectRecipe,
        isFavorite = isFavorite
    )
}