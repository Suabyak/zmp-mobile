package com.example.mobilesocialapp.utils

object Login {
    var loginError = ""

    fun validateLoginInput(
        username: String,
        password: String
    ): Boolean {
        if (username.isEmpty()) {
            loginError = "Please enter username"
            return false
        }

        if (password.isEmpty()) {
            loginError = "Please enter password"
            return false
        }

        loginError = ""
        return true
    }
}