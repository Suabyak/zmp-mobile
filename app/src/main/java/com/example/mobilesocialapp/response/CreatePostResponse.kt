package com.example.mobilesocialapp.response

data class CreatePostResponse(
    val message: String,
    val username: String,
    val creator: String,
    val selectedFile: String
)
