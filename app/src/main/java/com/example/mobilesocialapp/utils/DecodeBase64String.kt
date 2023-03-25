package com.example.mobilesocialapp.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class DecodeBase64String {
    public fun decodeBase64(imageString: String): Bitmap {
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }
}