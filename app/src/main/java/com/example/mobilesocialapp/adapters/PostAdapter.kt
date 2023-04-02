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
import com.example.mobilesocialapp.databinding.PostBinding
import com.example.mobilesocialapp.fragments.CommentsFragment
import com.example.mobilesocialapp.fragments.DeletePostFragment
import com.example.mobilesocialapp.fragments.EditPostFragment
import com.example.mobilesocialapp.fragments.ProfileFragment
import com.example.mobilesocialapp.response.PostsResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.LikePost
import com.example.mobilesocialapp.utils.RedirectToFragment

class PostAdapter(val userId: String, val token: String) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val decodeBase64String = DecodeBase64String()
    private lateinit var editPostFragment: EditPostFragment
    private lateinit var deletePostFragment: DeletePostFragment
    private lateinit var commentsFragment: CommentsFragment
    private val redirectToFragment = RedirectToFragment()
    private val likePost = LikePost()

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
        if(likes.contains(userId)){
            notLikedImg.visibility = View.VISIBLE
            likedImg.visibility = View.GONE
            likes.remove(userId)
        } else {
            notLikedImg.visibility = View.GONE
            likedImg.visibility = View.VISIBLE
            likes.add(userId)
        }

        likesNumber.text = likes.size.toString()

        likePost.likePost(postId, token)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.apply {
            val currentPost = posts[position]
            editPostFragment = EditPostFragment(currentPost._id, userId)
            deletePostFragment = DeletePostFragment(currentPost._id, userId)
            commentsFragment = CommentsFragment(currentPost._id, userId, token)

            postUsername.text = currentPost.username
            postImg.setImageBitmap(decodeBase64String.decodeBase64(currentPost.selectedFile))
            postLikesNumber.text = currentPost.likes.size.toString()
            postMainText.text = currentPost.message

            if(userId == currentPost.creator) {
                editPostBtn.visibility = View.VISIBLE
                deletePostBtn.visibility = View.VISIBLE
            }

            if(currentPost.likes.contains(userId)){
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
        }

        redirectToFragment.redirectToFragment(holder.binding.editPostBtn, editPostFragment)
        redirectToFragment.redirectToFragment(holder.binding.deletePostBtn, deletePostFragment)
        redirectToFragment.redirectToFragment(holder.binding.postCommentImg, commentsFragment)
    }
}