package com.craftify.craftify_app.data.server.api

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("data")
	val data: Data? = null
) : Parcelable

@Parcelize
data class DetectionsItem(

	@field:SerializedName("score")
	val score: Float? = null,

	@field:SerializedName("box")
	val box: List<Float?>? = null,

	@field:SerializedName("class")
	val jsonMemberClass: String? = null
) : Parcelable

@Parcelize
data class Data(

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null,

	@field:SerializedName("detections")
	val detections: List<DetectionsItem?>? = null
) : Parcelable

@Parcelize
data class RecommendationsItem(

	@field:SerializedName("project_img")
	val projectImg: String? = null,

	@field:SerializedName("project_materials")
	val projectMaterials: String? = null,

	@field:SerializedName("project_name")
	val projectName: String? = null,

	@field:SerializedName("project_recipe")
	val projectRecipe: String? = null
) : Parcelable
