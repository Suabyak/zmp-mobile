package com.example.mobilesocialapp

import android.content.SharedPreferences
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.request.CreatePostRequest
import com.example.mobilesocialapp.response.AuthResponse
import com.example.mobilesocialapp.response.CreatePostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/user/signinspecial")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/posts/createPost")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest,
                           @Header("Authorization") token: String): Response<CreatePostResponse>
}