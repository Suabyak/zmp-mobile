package com.example.mobilesocialapp

import com.example.mobilesocialapp.request.*
import com.example.mobilesocialapp.response.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    @POST("/api/users/sign-in/")
    suspend fun signIn(@Body authRequest: AuthRequest): Response<AuthResponse>

    @POST("/api/users/sign-up/")
    suspend fun signUp(@Body signUpRequest: SignUpRequest): Response<SignUpResponse>

    @GET("/api/users/get-user-data/")
    suspend fun getUserDataProfile(@Query("id") key: String): Response<UserDataProfileResponse>

    @POST("/api/posts/create/")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest,
                           @Header("Authorization") token: String): Response<CreatePostResponse>

    @GET("/api/posts/user-get/{id}/")
    suspend fun getUserPosts(@Path("id") key: String): Response<List<PostsResponse>>

    @GET("/api/posts/get/{id}/")
    suspend fun getPostById(@Path("id") key: String): Response<PostResponse>

    @PATCH("/api/posts/update/{id}/")
    suspend fun updatePost(@Body editPostRequest: EditPostRequest, @Header("Authorization") token: String, @Path("id") key: String)

    @DELETE("/api/post/{id}/")
    suspend fun deletePostById(@Path("id") key: String, @Header("Authorization") token: String)

    @POST("/api/post/like/{id}/")
    suspend fun likePost(@Path("id") key: Int, @Header("Authorization") token: String)

    @GET("/api/post/{id}/comments/")
    suspend fun getComments(@Path("id") key: String): Response<List<CommentsResponse>>

    @POST("/api/post/{id}/comment/")
    suspend fun addComment(@Path("id") key: String, @Header("Authorization") token: String, @Body addCommentRequest: AddCommentRequest)

    @GET("/api/users/get-users-by-search/")
    suspend fun getUsersBySearch(@Query("search") key: String): Response<List<UsersBySearchResponse>>

    @POST("/api/user/profile/set/")
    suspend fun changeProfile(@Body changeProfileRequest: ChangeProfileRequest, @Header("Authorization") token: String): Response<ChangeProfileResponse>

    @GET("/api/posts/get-feed")
    suspend fun getFeed(@Header("Authorization") token: String): Response<List<PostsResponse>>

    @GET("/api/user/observed/")
    suspend fun getObserved(@Header("Authorization") token: String): Response<List<GetObservedResponse>>

    @POST("/api/user/observe/")
    suspend fun observe(@Body observeRequest: ObserveRequest, @Header("Authorization") token: String)

}