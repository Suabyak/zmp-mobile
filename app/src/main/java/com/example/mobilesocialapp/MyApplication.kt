package com.example.mobilesocialapp

import android.app.Application
import android.provider.CalendarContract.Instances

class MyApplication: Application() {
        init {
            instance = this
        }
//    companion object {
//        @get:Synchronized
//        lateinit var instance: MyApplication
//    }
//
//    override fun onCreate(){
//        super.onCreate()
//        instance = this
//    }
    companion object {
    private lateinit var instance: MyApplication
        fun getInstance(): MyApplication {
            return instance
        }
    }
}