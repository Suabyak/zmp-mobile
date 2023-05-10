package com.example.mobilesocialapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.PostBinding
import com.example.mobilesocialapp.fragments.CommentsFragment
import com.example.mobilesocialapp.fragments.DeletePostFragment
import com.example.mobilesocialapp.fragments.EditPostFragment
import com.example.mobilesocialapp.response.PostsResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.LikePost
import com.example.mobilesocialapp.utils.RedirectToFragment

class PostAdapter(val currentLoggedUserId: String, val userId: String, val token: String) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val decodeBase64String = DecodeBase64String()
    private val editPostFragment = EditPostFragment()
    private val deletePostFragment = DeletePostFragment()
    private val commentsFragment = CommentsFragment()
    private val redirectToFragment = RedirectToFragment()
    private val likePost = LikePost()
    private val bundleData = Bundle()

    inner class PostViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<PostsResponse>() {
        override fun areItemsTheSame(oldItem: PostsResponse, newItem: PostsResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PostsResponse, newItem: PostsResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<PostsResponse>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    fun checkIsLiked(notLikedImg: ImageView, likedImg: ImageView, likes: ArrayList<String>, likesNumber: TextView, postId: String) {
        if(likes.contains(currentLoggedUserId)){
            notLikedImg.visibility = View.VISIBLE
            likedImg.visibility = View.GONE
            likes.remove(currentLoggedUserId)
        } else {
            notLikedImg.visibility = View.GONE
            likedImg.visibility = View.VISIBLE
            likes.add(currentLoggedUserId)
        }

        likesNumber.text = likes.size.toString()

        likePost.likePost(postId, token)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        bundleData.putString(BundleConsts.BundleToken, token)
        bundleData.putString(BundleConsts.BundleUserId, userId)
        bundleData.putString(BundleConsts.BundleCurrentLoggedUserId, currentLoggedUserId)

        holder.binding.apply {
            val currentPost = posts[position]

            postUsername.text = currentPost.username
            postImg.setImageBitmap(decodeBase64String.decodeBase64(currentPost.selectedFile))
            postLikesNumber.text = currentPost.likes.size.toString()
            postMainText.text = currentPost.message

            if(currentLoggedUserId == currentPost.creator) {
                editPostBtn.visibility = View.VISIBLE
                deletePostBtn.visibility = View.VISIBLE
            }

            if(currentPost.likes.contains(currentLoggedUserId)){
                postLikedImg.visibility = View.VISIBLE
            } else {
                postLikeImg.visibility = View.VISIBLE
            }

            postLikeImg.setOnClickListener {
                checkIsLiked(postLikeImg, postLikedImg, currentPost.likes, postLikesNumber, currentPost._id)
            }

            postLikedImg.setOnClickListener {
                checkIsLiked(postLikeImg, postLikedImg, currentPost.likes, postLikesNumber, currentPost._id)
            }

            postCommentImg.setOnClickListener { v ->
                bundleData.putString(BundleConsts.BundleCurrentPostId, currentPost._id)
                commentsFragment.arguments = bundleData
                redirectToFragment.redirect(v, commentsFragment)
            }

            editPostBtn.setOnClickListener { v ->
                bundleData.putString(BundleConsts.BundleCurrentPostId, currentPost._id)
                editPostFragment.arguments = bundleData
                redirectToFragment.redirect(v, editPostFragment)
            }

            deletePostBtn.setOnClickListener { v ->
                bundleData.putString(BundleConsts.BundleCurrentPostId, currentPost._id)
                deletePostFragment.arguments = bundleData
                redirectToFragment.redirect(v, deletePostFragment)
            }
        }
    }
}