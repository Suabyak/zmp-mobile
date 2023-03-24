package com.example.mobilesocialapp.request

data class EditPostRequest(
    val postId: String,
    val message: String,
    val selectedFile: String
)
