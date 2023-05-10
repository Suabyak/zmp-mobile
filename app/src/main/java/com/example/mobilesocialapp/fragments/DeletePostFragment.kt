package com.example.mobilesocialapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentDeletePostBinding
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.IOException

class DeletePostFragment() : Fragment() {
    private var _binding: FragmentDeletePostBinding? = null
    private val binding get() = _binding!!
    private val redirectToFragment = RedirectToFragment()
    private val profileFragment = ProfileFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDeletePostBinding.inflate(inflater, container, false)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()
        val userId = data?.get(BundleConsts.BundleUserId).toString()
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()
        val currentPostId = data?.get(BundleConsts.BundleCurrentPostId).toString()

        val bundleData = Bundle()
        bundleData.putString(BundleConsts.BundleToken, token)
        bundleData.putString(BundleConsts.BundleUserId, userId)
        bundleData.putString(BundleConsts.BundleCurrentLoggedUserId, currentLoggedUserId)

        binding.comeBackImg.setOnClickListener { v ->
            profileFragment.arguments = bundleData
            redirectToFragment.redirect(v, profileFragment)
        }

        binding.deletePostNo.setOnClickListener { v ->
            profileFragment.arguments = bundleData
            redirectToFragment.redirect(v, profileFragment)
        }

        binding.deletePostYes.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE

            lifecycleScope.launchWhenCreated {
                val response = try {
                    RetrofitInstance.api.deletePostById(currentPostId)
                } catch(e: IOException) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = BadResponses.notInternetConnection
                    return@launchWhenCreated
                } catch(e: HttpException) {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = BadResponses.unexpectedResponse
                    return@launchWhenCreated
                }

                if(response.isSuccessful && response.body() != null) {
                    binding.progressBar.visibility = View.INVISIBLE
                    profileFragment.arguments = bundleData
                    redirectToFragment.redirect(binding.root, profileFragment)
                } else {
                    binding.progressBar.visibility = View.INVISIBLE
                    binding.deletePostMessage.text = "Cant delete this post"
                }
            }
        }

        return binding.root
    }
}