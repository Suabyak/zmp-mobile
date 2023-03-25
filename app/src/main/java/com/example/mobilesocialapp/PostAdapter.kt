package com.example.mobilesocialapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.databinding.PostBinding
import com.example.mobilesocialapp.fragments.DeletePostFragment
import com.example.mobilesocialapp.fragments.EditPostFragment
import com.example.mobilesocialapp.response.PostsResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.RedirectToFragment


class PostAdapter(val userId: String) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private val decodeBase64String = DecodeBase64String()
    private lateinit var editPostFragment: EditPostFragment
    private lateinit var deletePostFragment: DeletePostFragment
    private val redirectToFragment = RedirectToFragment()

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

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.apply {
            val currentPost = posts[position]
            editPostFragment = EditPostFragment(currentPost._id, userId)
            deletePostFragment = DeletePostFragment(currentPost._id, userId)

            postUsername.text = currentPost.username
            postImg.setImageBitmap(decodeBase64String.decodeBase64(currentPost.selectedFile))
            postLikesNumber.text = currentPost.likes.size.toString()
            postMainText.text = currentPost.message

            if(userId == currentPost.creator) {
                editPostBtn.visibility = View.VISIBLE
                deletePostBtn.visibility = View.VISIBLE
            }
        }

        redirectToFragment.redirectToFragment(holder.binding.editPostBtn, editPostFragment)
        redirectToFragment.redirectToFragment(holder.binding.deletePostBtn, deletePostFragment)
    }
}