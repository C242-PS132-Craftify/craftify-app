package com.craftify.craftify_app.data.server.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DetailResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null
) : Parcelable

@Parcelize
data class DataItem(

	@field:SerializedName("project_img")
	val projectImg: String? = null,

	@field:SerializedName("project_materials")
	val projectMaterials: List<String?>? = null,

	@field:SerializedName("project_name")
	val projectName: String? = null,

	@field:SerializedName("project_recipe")
	val projectRecipe: List<String?>? = null
) : Parcelable
