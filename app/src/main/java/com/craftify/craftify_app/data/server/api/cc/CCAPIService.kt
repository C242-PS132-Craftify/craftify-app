package com.craftify.craftify_app.data.server.api.cc

import GetAllBlogResponse
import GetAllBlogResponseItem
import com.craftify.craftify_app.data.server.api.cc.response.DeleteBlogResponse
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface CCAPIService {
    @GET("blog")
    fun getAllBLog(): Call<List<GetAllBlogResponseItem>>

    @GET("blog/{id}")
    fun getBlogDetails(
        @Path("id") id: String
    ): Call<GetAllBlogResponseItem>

    @Multipart
    @POST("upload/blog-image")
    fun uploadBlogImage(
        @Part image: MultipartBody.Part
    ): Call<UploadHeaderImageResponse>

    @FormUrlEncoded
    @POST("blog")
    fun addBlog(
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("content") content: String,
        @Field("header_image") headerImage: String,
    ): Call<GetAllBlogResponseItem>

    @FormUrlEncoded
    @PUT("blog/{id}")
    fun editBlog(
        @Path("id") id: String,
        @Field("title") title: String,
        @Field("author") author: String,
        @Field("content") content: String,
        @Field("header_image") headerImage: String,
    ): Call<GetAllBlogResponseItem>

    @DELETE("blog/{id}")
    fun deleteBlog(
        @Path("id") id: String
    ): Call<DeleteBlogResponse>
}