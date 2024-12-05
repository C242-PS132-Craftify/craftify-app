package com.craftify.craftify_app.data.server.api.cc

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CCApiConfig {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl("https://craftify-backend-677363691640.asia-southeast2.run.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
}