package com.example.mobilesocialapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobilesocialapp.DashboardActivity
import com.example.mobilesocialapp.MainActivity
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val sharedPreferences = this.requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()

        if (sharedPreferences.getString("jwt", null) == null) {
            redirectToLogin()
        }

        binding.logout.setOnClickListener {
            sharedPreferences.edit().putString("jwt", null).apply()
            redirectToLogin()
        }

        return binding.root
    }

    private fun redirectToLogin() {
        val intent = Intent(this.requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

}