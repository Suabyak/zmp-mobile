package com.example.mobilesocialapp

import android.content.SharedPreferences
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.request.CreatePostRequest
import com.example.mobilesocialapp.response.AuthResponse
import com.example.mobilesocialapp.response.CreatePostResponse
import com.example.mobilesocialapp.response.PostResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/user/signinspecial")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/posts/createPost")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest,
                           @Header("Authorization") token: String): Response<CreatePostResponse>

    @GET("/posts/getUserPostsSpecial")
    suspend fun getUserPosts(@Header("Authorization") token: String): Response<List<PostResponse>>
}