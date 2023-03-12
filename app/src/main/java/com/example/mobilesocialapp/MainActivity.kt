package com.example.mobilesocialapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import com.example.mobilesocialapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val username = binding.usernameLoginText.text.toString()
            val password = binding.passwordLoginText.text.toString()

            if(TextUtils.isEmpty(username)) {
                binding.usernameLoginText.setError("Please enter username")
            } else if(TextUtils.isEmpty(password)) {
                binding.passwordLoginText.setError("Please enter password")
            } else {
                println(username)
                println(password)
            }
        }
    }
}