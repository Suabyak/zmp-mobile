package com.example.mobilesocialapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.mobilesocialapp.databinding.ActivitySignupBinding
import com.example.mobilesocialapp.validations.emailValidate
import com.example.mobilesocialapp.validations.emptyInput
import com.example.mobilesocialapp.validations.passwordValidate
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val emailValidate = emailValidate()
    private val passwordValidate = passwordValidate()
    private val emptyInput = emptyInput()

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

            if(!emailValidate.checkIsEmailValidate(email)) {
                binding.emailSignUpText.error = "Invalid email address"
            }
            else if(!passwordValidate.checkPasswordLength(password)) {
                binding.passwordSignupText.error = "Password must contains at least 8 characters"
            }
            else if(!emptyInput.checkIsEmptyInput(username)) {
                binding.usernameSignupText.error = "Please enter username"
            }
            else if(!passwordValidate.checkPasswordContainsBigLetter(password)) {
                binding.passwordSignupText.error = "Password must contains at least one big letter"
            }
            else if(!passwordValidate.checkPasswordContainsNumber(password)) {
                binding.passwordSignupText.error = "Password must contains at least one number"
            } else {
                println("correct")
            }
        }
    }

    private fun isValidateEmail(email: String): Boolean {
        val pattern = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

}