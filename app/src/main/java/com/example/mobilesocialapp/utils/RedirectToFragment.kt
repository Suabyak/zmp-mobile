package com.example.mobilesocialapp.utils

import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mobilesocialapp.R

class RedirectToFragment {

    fun redirect(v: View, fragment: Fragment) {
        val a = v!!.context as AppCompatActivity
        a.supportFragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit()
    }
}