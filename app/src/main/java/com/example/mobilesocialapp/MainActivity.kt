package com.example.mobilesocialapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.databinding.ActivityMainBinding
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.utils.Login
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

            if(!Login.validateLoginInput(username, password)) {
                binding.loginMessage.text = Login.loginError
            } else {
                binding.loginMessage.text = ""
                binding.progressBar.visibility = View.VISIBLE

                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newAuthRequest = AuthRequest(username, password)
                        RetrofitInstance.api.signIn(newAuthRequest)
                    } catch(e: IOException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.loginMessage.text = BadResponses.notInternetConnection
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.loginMessage.text = BadResponses.unexpectedResponse
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        binding.progressBar.visibility = View.INVISIBLE

                        sharedPreferences.edit()
                            .putString("jwt", response.body()!!.message)
                            .apply()
                        sharedPreferences.edit()
                            .putString("userId", response.body()!!.userId)
                            .apply()

                        val intent = Intent(this@MainActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.loginMessage.text = "Incorrect credentials"
                    }
                }
            }
        }
    }
}