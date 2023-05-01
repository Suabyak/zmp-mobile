package com.example.mobilesocialapp.utils

import java.util.regex.Pattern

object Registration {
    val emailPattern = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}\$")
    val passwordLength = 8
    var registrationError = ""

    fun validateRegistrationInput(
        email: String,
        password: String,
        username: String
    ): Boolean {
        if (!emailPattern.matcher(email).matches()) {
            registrationError = "Invalid email address"
            return false
        }

        if (username.isEmpty()) {
            registrationError = "Please enter username"
            return false
        }

        if (password.length < passwordLength) {
            registrationError = "Password must contains at least $passwordLength characters"
            return false
        }

        if (!password.contains("[A-Z]".toRegex())) {
            registrationError = "Password must contains at least one big letter"
            return false
        }

        if (!password.contains("[0-9]".toRegex())) {
            registrationError = "Password must contains at least one number"
            return false
        }
        registrationError = ""
        return true
    }
}