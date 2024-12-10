package com.craftify.craftify_app.utils

import android.content.Context
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object CreateMultipartBody {
    fun fromFile(file: File, fieldName: String = "image"): MultipartBody.Part {
        val mimeType = file.extension.toMimeType()
        val requestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(fieldName, file.name, requestBody)
    }

    fun fromUri(context: Context, uri: Uri, fieldName: String = "image"): MultipartBody.Part {
        val file = FileHelpers.getFileFromUri(context, uri)
            ?: throw IllegalArgumentException("Unable to resolve URI to a File")
        return fromFile(file, fieldName)
    }

    private fun String.toMimeType(): String {
        return when (this.lowercase()) {
            "jpg", "jpeg" -> "image/jpeg"
            "png" -> "image/png"
            "gif" -> "image/gif"
            "webp" -> "image/webp"
            else -> "application/octet-stream"
        }
    }
}
