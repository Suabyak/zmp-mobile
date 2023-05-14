package com.example.mobilesocialapp.response

import com.example.mobilesocialapp.utils.User

data class PostsResponse(
    val id: Int,
    val body: String,
    val user: User,
    val file: String,
    val likes: ArrayList<Int>
)
