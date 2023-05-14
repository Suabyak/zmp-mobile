package com.example.mobilesocialapp.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.DashboardActivity
import com.example.mobilesocialapp.MainActivity
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.adapters.PostAdapter
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentHomeBinding
import retrofit2.HttpException
import java.io.IOException


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val sharedPreferences = this.requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()
        val userId = data?.get(BundleConsts.BundleUserId).toString()
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()

        setupRecyclerView(currentLoggedUserId, userId, token)

        if (sharedPreferences.getString("jwt", null) == null) {
            redirectToLogin()
        }

        binding.logout.setOnClickListener {
            sharedPreferences.edit().putString("jwt", null).apply()
            redirectToLogin()
        }

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getFeed("Bearer $token")
            } catch(e: IOException) {
                binding.errorMessage.text = BadResponses.notInternetConnection
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.errorMessage.text = BadResponses.unexpectedResponse
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                postAdapter.posts = response.body()!!
            } else {
                binding.errorMessage.text = "Cant retrieve posts"
            }
        }

        return binding.root
    }

    private fun redirectToLogin() {
        val intent = Intent(this.requireActivity(), MainActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecyclerView(currentLoggedUserId: String, userId: String, token: String) = binding.rvPosts.apply {
        postAdapter = PostAdapter(currentLoggedUserId.toInt(), userId, token)
        adapter = postAdapter
        layoutManager = LinearLayoutManager(this@HomeFragment.context)
    }

}