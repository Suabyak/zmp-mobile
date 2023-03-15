package com.example.mobilesocialapp

import android.content.Context
import android.content.SharedPreferences
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    val sharedPreferences: SharedPreferences

    @POST("/user/signinspecial")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>
}