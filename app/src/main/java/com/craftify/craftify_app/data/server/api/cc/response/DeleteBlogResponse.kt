package com.craftify.craftify_app.data.server.api.cc.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DeleteBlogResponse(

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
