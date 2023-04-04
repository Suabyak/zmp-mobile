package com.example.mobilesocialapp.utils

import android.os.Bundle
import androidx.fragment.app.Fragment

class ReturnBundleData {
    private val bundleData = Bundle()

    fun returnData(userId: String, token: String, fragment: Fragment) {
        bundleData.putString("userId", userId)
        bundleData.putString("token", token)
        fragment.arguments = bundleData
    }
}