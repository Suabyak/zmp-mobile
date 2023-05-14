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
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentProfileBinding
import com.example.mobilesocialapp.request.ObserveRequest
import com.example.mobilesocialapp.response.GetObservedResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import retrofit2.HttpException
import java.io.IOException

class ProfileFragment() : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter
    private val decodeBase64String = DecodeBase64String()
    private lateinit var observes: List<GetObservedResponse>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()
        val userId = data?.get(BundleConsts.BundleUserId).toString()
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()

        setupRecyclerView(currentLoggedUserId, userId, token)

        if (currentLoggedUserId != userId) {
            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.getObserved("Bearer $token")
                } catch(e: IOException) {
                    return@launchWhenCreated
                } catch(e: HttpException) {
                    return@launchWhenCreated
                }

                if(response.isSuccessful && response.body() != null) {
                    observes = response.body()!!

                    if (observes.isEmpty()) {
                        binding.notObserved.visibility = View.VISIBLE
                    } else {
                        for (item in observes) {
                            if (item.observed.id == userId.toInt()) {
                                setButtonToObserve()
                            } else {
                                setButtonToNotObserve()
                            }
                        }
                    }
                }
            }

            val newObserveRequest = ObserveRequest(userId.toInt())

            binding.notObserved.setOnClickListener {
                setButtonToObserve()
                Observe(newObserveRequest, "Bearer $token")
            }

            binding.observed.setOnClickListener {
                setButtonToNotObserve()
                Observe(newObserveRequest, "Bearer $token")
            }
        }

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
                if(response.body()!!.file.isNotEmpty()) {
                    binding.userProfileImg.setImageBitmap(decodeBase64String.decodeBase64(response.body()!!.file))
                }
            } else {
                binding.userPostsMessage.text = "Cant retrieve user"
            }
        }

        lifecycleScope.launchWhenCreated {
            binding.progressBar.visibility = View.VISIBLE

            val response = try {
                RetrofitInstance.api.getUserPosts(userId)
            } catch(e: IOException) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.userPostsMessage.text = BadResponses.notInternetConnection
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.progressBar.visibility = View.INVISIBLE
                binding.userPostsMessage.text = BadResponses.unexpectedResponse
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                binding.progressBar.visibility = View.INVISIBLE
                postAdapter.posts = response.body()!!

                if (postAdapter.itemCount == 0) {
                    binding.userPostsMessage.text = "No posts yet"
                }
            } else {
                binding.userPostsMessage.text = "Cant retrieve posts"
            }
        }

        return binding.root
    }

    private fun setupRecyclerView(currentLoggedUserId: String, userId: String, token: String) = binding.rvPosts.apply {
        postAdapter = PostAdapter(currentLoggedUserId.toInt(), userId, token)
        adapter = postAdapter
        layoutManager = LinearLayoutManager(this@ProfileFragment.context)
    }

    private fun Observe(observeRequest: ObserveRequest, token: String) {
        lifecycleScope.launchWhenCreated {
            try {
                RetrofitInstance.api.observe(observeRequest, token)
            } catch(e: IOException) {
                return@launchWhenCreated
            } catch(e: HttpException) {
                return@launchWhenCreated
            }
        }
    }

    private fun setButtonToObserve() {
        binding.observed.visibility = View.VISIBLE
        binding.notObserved.visibility = View.GONE
    }

    private fun setButtonToNotObserve() {
        binding.observed.visibility = View.GONE
        binding.notObserved.visibility = View.VISIBLE
    }
}