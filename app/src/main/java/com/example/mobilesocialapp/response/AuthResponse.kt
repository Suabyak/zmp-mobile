package com.example.mobilesocialapp.response

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val id: Int,
    val token: String
)
