package com.example.mobilesocialapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.R
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.adapters.CommentAdapter
import com.example.mobilesocialapp.adapters.PostAdapter
import com.example.mobilesocialapp.databinding.FragmentCommentsBinding
import com.example.mobilesocialapp.request.AddCommentRequest
import com.example.mobilesocialapp.response.CommentsResponse
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.IOException

class CommentsFragment(val postId: String, val userId: String, val token: String) : Fragment() {
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

        setupRecyclerView()

        val bundleUserId = Bundle()
        bundleUserId.putString("userId", userId)
        profileFragment.arguments = bundleUserId

        redirectToFragment.redirectToFragment(binding.comeBackImg, profileFragment)

        binding.sendComment.setOnClickListener {
            val commentValue = binding.commentInput.text.toString()

            if(commentValue.isNotEmpty()){
                val newComment = AddCommentRequest(commentValue)
                addComment(newComment)
            }
        }

        getComments()

        return binding.root
    }

    private fun setupRecyclerView() = binding.rvComments.apply {
        commentAdapter = CommentAdapter()
        adapter = commentAdapter
        layoutManager = LinearLayoutManager(this@CommentsFragment.context)
    }

    private fun getComments() {
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getComments(postId)
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

    private fun addComment(newComment: AddCommentRequest) {
        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.addComment(postId, "Bearer $token", newComment)
            } catch(e: IOException) {
                return@launchWhenCreated
            } catch(e: HttpException) {
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                getComments()
            } else {
                println("Cant add comment")
            }
        }
    }
}