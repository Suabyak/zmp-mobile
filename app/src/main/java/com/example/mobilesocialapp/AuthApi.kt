package com.example.mobilesocialapp

import com.example.mobilesocialapp.request.AddCommentRequest
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.request.CreatePostRequest
import com.example.mobilesocialapp.request.EditPostRequest
import com.example.mobilesocialapp.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/user/signinspecial")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/posts/createPost")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest,
                           @Header("Authorization") token: String): Response<CreatePostResponse>

    @GET("/posts/getUserPostsSpecial")
    suspend fun getUserPosts(@Query("key") key: String): Response<List<PostsResponse>>

    @GET("/user/getUserDataProfileSpecial")
    suspend fun getUserDataProfile(@Query("key") key: String): Response<UserDataProfileResponse>

    @GET("/posts/getPostByIdSpecial")
    suspend fun getPostById(@Query("id") key: String): Response<PostResponse>

    @PATCH("/posts/updatePostSpecial")
    suspend fun updatePost(@Body editPostRequest: EditPostRequest): Response<EditPostResponse>

    @DELETE("/posts/deletePostSpecial")
    suspend fun deletePostById(@Query("id") key: String): Response<DeletePostResponse>

    @GET("/posts/getCommentsByIdSpecial")
    suspend fun getComments(@Query("id") key: String): Response<List<CommentsResponse>>

    @POST("/posts/addComment")
    suspend fun addComment(@Query("id") key: String, @Header("Authorization") token: String, @Body addCommentRequest: AddCommentRequest): Response<AddCommentResponse>

    @PATCH("/posts/likePostSpecial")
    suspend fun likePost(@Query("id") key: String, @Header("Authorization") token: String): Response<LikePostResponse>
}