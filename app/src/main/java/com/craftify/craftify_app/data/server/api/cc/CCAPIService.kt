package com.craftify.craftify_app.data.server.api.cc

import GetAllBlogResponse
import GetAllBlogResponseItem
import com.craftify.craftify_app.data.model.BlogRequest
import com.craftify.craftify_app.data.server.api.cc.response.DeleteBlogResponse
import com.craftify.craftify_app.data.server.api.cc.response.UploadHeaderImageResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
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

    @GET("blog/user/{user_id}")
    fun getUserBlogs(
        @Path("user_id") userId : String
    ) : Call<GetAllBlogResponse>

    @Multipart
    @POST("upload/blog-image")
    fun uploadBlogImage(
        @Part image: MultipartBody.Part
    ): Call<UploadHeaderImageResponse>

    @POST("blog")
    @Headers("Content-Type: application/json") // Optional: Explicitly set content type to application/json
    fun addBlog(@Body blogRequest: BlogRequest): Call<GetAllBlogResponseItem>

    @PUT("blog/{id}")
    @Headers("Content-Type: application/json") // Optional: Explicitly set content type to application/json
    fun editBlog(
        @Path("id") id: String,
        @Body blogRequest: BlogRequest
    ): Call<GetAllBlogResponseItem>

    @DELETE("blog/{id}")
    fun deleteBlog(
        @Path("id") id: String
    ): Call<DeleteBlogResponse>
}