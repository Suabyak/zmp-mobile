package com.example.mobilesocialapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.databinding.CommentBinding
import com.example.mobilesocialapp.databinding.PostBinding
import com.example.mobilesocialapp.fragments.DeletePostFragment
import com.example.mobilesocialapp.fragments.EditPostFragment
import com.example.mobilesocialapp.response.CommentsResponse
import com.example.mobilesocialapp.response.PostsResponse

class CommentAdapter: RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    inner class CommentViewHolder(val binding: CommentBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<CommentsResponse>() {
        override fun areItemsTheSame(oldItem: CommentsResponse, newItem: CommentsResponse): Boolean {
            return oldItem.commentCreator == newItem.commentCreator
        }

        override fun areContentsTheSame(oldItem: CommentsResponse, newItem: CommentsResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var comments: List<CommentsResponse>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return comments.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentViewHolder {
        return CommentViewHolder(
            CommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        holder.binding.apply {
            val currentComment = comments[position]

            commentCreator.text = currentComment.commentCreator
            commentValue.text = currentComment.value
        }
    }
}