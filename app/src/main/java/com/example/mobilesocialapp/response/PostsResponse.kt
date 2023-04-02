package com.example.mobilesocialapp.response

data class PostsResponse(
    val _id: String,
    val message: String,
    val username: String,
    val creator: String,
    val selectedFile: String,
    val likes: ArrayList<String>
)
