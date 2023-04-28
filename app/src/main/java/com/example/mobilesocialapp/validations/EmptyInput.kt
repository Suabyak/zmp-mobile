package com.example.mobilesocialapp.validations

class EmptyInput {
    fun checkIsEmptyInput(inputValue: String): Boolean {
        if(inputValue.isEmpty()) {
            return false
        }
        return true
    }
}