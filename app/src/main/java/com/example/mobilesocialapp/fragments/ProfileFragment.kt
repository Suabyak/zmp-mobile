package com.example.mobilesocialapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.*
import com.example.mobilesocialapp.adapters.PostAdapter
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.databinding.FragmentProfileBinding
import com.example.mobilesocialapp.utils.DecodeBase64String
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment(val currentLoggedUserId: String, val userId: String, val token: String) : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter
    private val decodeBase64String = DecodeBase64String()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupRecyclerView(currentLoggedUserId, userId, token)

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getUserDataProfile(userId)
            } catch(e: IOException) {
                binding.userPostsMessage.text = BadResponses.notInternetConnection
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.userPostsMessage.text = BadResponses.unexpectedResponse
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                binding.usernameProfile.text = response.body()!!.username
                if(response.body()!!.selectedFile.isNotEmpty()) {
                    binding.userProfileImg.setImageBitmap(decodeBase64String.decodeBase64(response.body()!!.selectedFile))
                }
            } else {
                binding.userPostsMessage.text = "Cant retrieve user"
            }
        }

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getUserPosts(userId)
            } catch(e: IOException) {
                binding.userPostsMessage.text = BadResponses.notInternetConnection
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.userPostsMessage.text = BadResponses.unexpectedResponse
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

    private fun setupRecyclerView(currentLoggedUserId: String, userId: String, token: String) = binding.rvPosts.apply {
        postAdapter = PostAdapter(currentLoggedUserId, userId, token)
        adapter = postAdapter
        layoutManager = LinearLayoutManager(this@ProfileFragment.context)
    }
}