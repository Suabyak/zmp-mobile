package com.example.mobilesocialapp.request

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String,
    val password_confirm: String
)