package com.example.mobilesocialapp.utils

import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.mobilesocialapp.MyApplication
import org.json.JSONObject

class LikePost {
    private val likePostUrl: String = "http://192.168.43.106:5000/posts/likePostSpecial"

    fun likePost(postId: String, token: String){
        val reqQueue: RequestQueue = Volley.newRequestQueue(MyApplication.getInstance())
        val request = object: StringRequest(Method.POST, likePostUrl, { result ->
            Log.d("Result", result.toString())
        }, { err ->
            Log.d("Error", err.message.toString())
        })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer $token"
                return headers
            }

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = postId
                return params
            }
        }

        reqQueue.add(request)
    }
}