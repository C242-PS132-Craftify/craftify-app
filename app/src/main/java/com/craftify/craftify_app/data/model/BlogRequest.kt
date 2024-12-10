package com.craftify.craftify_app.data.model

data class BlogRequest(
    val title: String,
    val author: String,
    val user_id: String,
    val content: String,
    val header_image: String
)
