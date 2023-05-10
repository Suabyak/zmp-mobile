package com.example.mobilesocialapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.100.3:5000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

}