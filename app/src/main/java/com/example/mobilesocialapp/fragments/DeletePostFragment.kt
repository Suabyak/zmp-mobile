package com.example.mobilesocialapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.databinding.FragmentDeletePostBinding
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.IOException

class DeletePostFragment(val postId: String, val userId: String, val token: String) : Fragment() {
    private var _binding: FragmentDeletePostBinding? = null
    private val binding get() = _binding!!
    private val redirectToFragment = RedirectToFragment()
    private val profileFragment = ProfileFragment(userId, userId, token)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletePostBinding.inflate(inflater, container, false)

        binding.comeBackImg.setOnClickListener { v ->
            redirectToFragment.redirect(v, profileFragment)
        }

        binding.deletePostNo.setOnClickListener { v ->
            redirectToFragment.redirect(v, profileFragment)
        }

        binding.deletePostYes.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.deletePostById(postId)
                } catch(e: IOException) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = "You might not have internet connection"
                    return@launchWhenCreated
                } catch(e: HttpException) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = "Unexpected response"
                    return@launchWhenCreated
                }

                if(response.isSuccessful && response.body() != null) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = response.body()!!.message
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = "Cant delete this post"
                }
            }
        }

        return binding.root
    }
}