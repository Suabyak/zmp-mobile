package com.example.mobilesocialapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("jwt", Context.MODE_PRIVATE)
        setContentView(binding.root)

        binding.singupTextLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameLoginText.text.toString()
            val password = binding.passwordLoginText.text.toString()

            if(TextUtils.isEmpty(username)) {
                binding.usernameLoginText.setError("Please enter username")
            } else if(TextUtils.isEmpty(password)) {
                binding.passwordLoginText.setError("Please enter password")
            } else {
                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newAuthRequest = AuthRequest(username, password)
                        RetrofitInstance.api.signIn(newAuthRequest)
                    } catch(e: IOException) {
                        println(e)
                        Log.e(TAG, "you might not have internet connection")
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        Log.e(TAG, "unexpected response")
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        println("Zalogowano")
                        sharedPreferences.edit()
                            .putString("jwt", response.body()!!.message)
                            .apply()
                    } else {
                        Log.e(TAG, "Response not successful")
                    }
                }
            }
        }
    }
}