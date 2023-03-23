package com.example.mobilesocialapp.response

data class PostResponse(
    val _id: String,
    val message: String,
    val username: String,
    val selectedFile: String,
    val likes: List<String>
)
