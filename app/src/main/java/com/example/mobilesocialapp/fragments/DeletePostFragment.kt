package com.example.mobilesocialapp.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.R
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.databinding.FragmentDeletePostBinding
import com.example.mobilesocialapp.databinding.FragmentProfileBinding
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.IOException

class DeletePostFragment(val postId: String, val userId: String) : Fragment() {
    private var _binding: FragmentDeletePostBinding? = null
    private val binding get() = _binding!!
    private val redirectToFragment = RedirectToFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletePostBinding.inflate(inflater, container, false)

        val bundleUserId = Bundle()
        bundleUserId.putString("userId", userId)
        profileFragment.arguments = bundleUserId

        redirectToFragment.redirectToFragment(binding.comeBackImg, profileFragment)
        redirectToFragment.redirectToFragmentByButton(binding.deletePostNo, profileFragment)

        binding.deletePostYes.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.deletePostById(postId)
                } catch(e: IOException) {
                    binding.deletePostMessage.text = "You might not have internet connection"
                    return@launchWhenCreated
                } catch(e: HttpException) {
                    binding.deletePostMessage.text = "Unexpected response"
                    return@launchWhenCreated
                }

                if(response.isSuccessful && response.body() != null) {
                    binding.deletePostMessage.text = response.body()!!.message
                } else {
                    binding.deletePostMessage.text = "Cant delete this post"
                }
            }
        }

        return binding.root
    }
}