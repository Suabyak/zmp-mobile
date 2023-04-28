package com.example.mobilesocialapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.databinding.ActivityMainBinding
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.validations.emptyInput
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val emptyInput = emptyInput()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val sharedPreferences: SharedPreferences = this.getSharedPreferences("prefs", MODE_PRIVATE)
        setContentView(binding.root)

        binding.singupTextLogin.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameLoginText.text.toString()
            val password = binding.passwordLoginText.text.toString()

            if(!emptyInput.checkIsEmptyInput(username)) {
                binding.usernameLoginText.error = "Please enter username"
            }
            else if(!emptyInput.checkIsEmptyInput(password)) {
                binding.passwordLoginText.error = "Please enter password"
            } else {
                binding.loginMessage.text = ""

                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newAuthRequest = AuthRequest(username, password)
                        RetrofitInstance.api.signIn(newAuthRequest)
                    } catch(e: IOException) {
                        binding.loginMessage.text = "You might not have internet connection"
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.loginMessage.text = "Unexpected response"
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        sharedPreferences.edit()
                            .putString("jwt", response.body()!!.message)
                            .apply()
                        sharedPreferences.edit()
                            .putString("userId", response.body()!!.userId)
                            .apply()

                        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.loginMessage.text = "Incorrect credentials"
                    }
                }
            }
        }
    }
}