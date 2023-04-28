package com.example.mobilesocialapp.validations

class PasswordValidate {
    val passwordLength = 8
    fun checkPasswordLength(password: String): Boolean {
        if(password.length < passwordLength) {
            return false
        }
        return true
    }

    fun checkPasswordContainsBigLetter(password: String): Boolean {
        if(!password.contains("[A-Z]".toRegex())) {
            return false
        }
        return true
    }

    fun checkPasswordContainsNumber(password: String): Boolean {
        if(!password.contains("[0-9]".toRegex())) {
            return false
        }
        return true
    }
}