package com.example.mobilesocialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.databinding.ActivitySignupBinding
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.request.SignUpRequest
import com.example.mobilesocialapp.utils.Registration
import retrofit2.HttpException
import java.io.IOException

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.loginTextSignUp.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.signupButton.setOnClickListener {
            val email = binding.emailSignUpText.text.toString()
            val password = binding.passwordSignupText.text.toString()
            val confirmPassword = binding.confirmPasswordSignupText.text.toString()
            val username = binding.usernameSignupText.text.toString()

            if (!Registration.validateRegistrationInput(email, password, confirmPassword, username)) {
                binding.signupError.text = Registration.registrationError
            } else {
                binding.signupError.text = Registration.registrationError
                binding.progressBar.visibility = View.VISIBLE

                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newSignUpRequest = SignUpRequest(username, email, password, confirmPassword)
                        RetrofitInstance.api.signUp(newSignUpRequest)
                    } catch(e: IOException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.signupError.text = BadResponses.notInternetConnection
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.signupError.text = BadResponses.unexpectedResponse
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.signupError.text = response.body()!!.message
                    } else {
                        println(response.isSuccessful)
                        println(response.body())
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.signupError.text = "Email or username already taken"
                    }
                }
            }
        }
    }
}