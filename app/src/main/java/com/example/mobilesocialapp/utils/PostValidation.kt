package com.example.mobilesocialapp.utils

object PostValidation {
    var postError = ""

    fun validatePostInput(
        description: String,
        imageString: String
    ) : Boolean {
        if (description.isEmpty()) {
            postError = "Description can't be empty!"
            return false
        }

        if (imageString.isEmpty()) {
            postError = "Please select an image!"
            return false
        }

        postError = ""
        return true
    }
}