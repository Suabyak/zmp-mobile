package com.example.mobilesocialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.mobilesocialapp.databinding.ActivitySignupBinding
import com.example.mobilesocialapp.utils.Registration

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
            val username = binding.usernameSignupText.text.toString()

            if (!Registration.validateRegistrationInput(email, password, username)) {
                binding.signupError.text = Registration.registrationError
            } else {
                binding.signupError.text = Registration.registrationError
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }
}