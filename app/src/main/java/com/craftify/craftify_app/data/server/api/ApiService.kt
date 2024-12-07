package com.craftify.craftify_app.data.server.api

import com.craftify.craftify_app.data.server.response.DetailResponse
import com.craftify.craftify_app.data.server.response.PredictResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @Multipart
    @POST("predict")
    fun uploadImage(
        @Part file: MultipartBody.Part
    ): Call<PredictResponse>

    @GET("get_project/{title}")
    fun getProject(
        @Path("title") title: String
    ): Call<DetailResponse>

    @GET("get_project")
    fun getAllProject(
    ): Call<DetailResponse>

}
