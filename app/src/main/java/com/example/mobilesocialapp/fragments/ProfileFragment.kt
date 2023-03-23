package com.example.mobilesocialapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.DashboardActivity
import com.example.mobilesocialapp.PostAdapter
import com.example.mobilesocialapp.R
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.databinding.FragmentCreateBinding
import com.example.mobilesocialapp.databinding.FragmentProfileBinding
import com.example.mobilesocialapp.request.AuthRequest
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        setupRecyclerView()

        val data = arguments
        val userId = data?.get("userId").toString()

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getUserPosts(userId)
            } catch(e: IOException) {
                binding.userPostsMessage.text = "You might not have internet connection"
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.userPostsMessage.text = "Unexpected response"
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                postAdapter.posts = response.body()!!
            } else {
                binding.userPostsMessage.text = "Cant retrieve posts"
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvPosts.apply {
        postAdapter = PostAdapter()
        adapter = postAdapter
        layoutManager = LinearLayoutManager(this@ProfileFragment.context)
    }
}