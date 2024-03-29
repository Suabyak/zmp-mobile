package com.example.mobilesocialapp.response

import com.example.mobilesocialapp.utils.User

data class CommentsResponse(
    val id: Int,
    val user: User,
    val body: String
)
