package com.example.mobilesocialapp.validations

class emptyInput {
    fun checkIsEmptyInput(inputValue: String): Boolean {
        if(inputValue.isEmpty()) {
            return false
        }
        return true
    }
}