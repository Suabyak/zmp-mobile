package com.example.mobilesocialapp

import android.app.PendingIntent.getActivity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.databinding.PostBinding
import com.example.mobilesocialapp.response.PostResponse
import java.io.ByteArrayOutputStream


class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(val binding: PostBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<PostResponse>() {
        override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<PostResponse>
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

    private fun decodeBase64(imageString: String): Bitmap {
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.apply {
            val currentPost = posts[position]
            postUsername.text = currentPost.username
            postImg.setImageBitmap(decodeBase64(currentPost.selectedFile))
            postLikesNumber.text = currentPost.likes.size.toString()
            postMainText.text = currentPost.message
        }
    }
}