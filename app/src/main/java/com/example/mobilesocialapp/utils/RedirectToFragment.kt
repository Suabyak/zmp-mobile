package com.example.mobilesocialapp.utils

import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobilesocialapp.R

class RedirectToFragment {
    fun redirectToFragment(imageView: ImageView, fragment: Fragment) {
        imageView.setOnClickListener { v ->
            val activity = v!!.context as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit()
        }
    }
}