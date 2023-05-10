package com.example.mobilesocialapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.adapters.CommentAdapter
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentCommentsBinding
import com.example.mobilesocialapp.request.AddCommentRequest
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.IOException

class CommentsFragment() : Fragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentAdapter: CommentAdapter
    private val profileFragment = ProfileFragment()
    private val redirectToFragment = RedirectToFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()
        val userId = data?.get(BundleConsts.BundleUserId).toString()
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()
        val currentPostId = data?.get(BundleConsts.BundleCurrentPostId).toString()

        val bundleData = Bundle()
        bundleData.putString(BundleConsts.BundleToken, token)
        bundleData.putString(BundleConsts.BundleUserId, userId)
        bundleData.putString(BundleConsts.BundleCurrentLoggedUserId, currentLoggedUserId)

        setupRecyclerView()

        binding.comeBackImg.setOnClickListener { v ->
            profileFragment.arguments = bundleData
            redirectToFragment.redirect(v, profileFragment)
        }

        binding.sendComment.setOnClickListener {
            val commentValue = binding.commentInput.text.toString()

            if(commentValue.isNotEmpty()){
                val newComment = AddCommentRequest(commentValue)
                addComment(newComment, currentPostId, token)
            }
        }

        getComments(currentPostId)

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvComments.apply {
        commentAdapter = CommentAdapter()
        adapter = commentAdapter
        layoutManager = LinearLayoutManager(this@CommentsFragment.context)
    }

    private fun getComments(currentPostId: String) {
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getComments(currentPostId)
            } catch(e: IOException) {
                return@launchWhenCreated
            } catch(e: HttpException) {
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                commentAdapter.comments = response.body()!!
            } else {
                println("Cant retrieve comments")
            }
        }
    }

    private fun addComment(newComment: AddCommentRequest, currentPostId: String, token: String) {
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.addComment(currentPostId, "Bearer $token", newComment)
            } catch(e: IOException) {
                return@launchWhenCreated
            } catch(e: HttpException) {
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                getComments(currentPostId)
            } else {
                println("Cant add comment")
            }
        }
    }
}