package com.example.mobilesocialapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.databinding.SearchedUserBinding
import com.example.mobilesocialapp.fragments.ProfileFragment
import com.example.mobilesocialapp.response.UsersBySearchResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.RedirectToFragment

class SearchUsersAdapter(val userId: String, val token: String) : RecyclerView.Adapter<SearchUsersAdapter.SearchedUserViewHolder>() {
    private val decodeBase64String = DecodeBase64String()
    private lateinit var profileFragment: ProfileFragment
    private val redirectToFragment = RedirectToFragment()

    inner class SearchedUserViewHolder(val binding: SearchedUserBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object: DiffUtil.ItemCallback<UsersBySearchResponse>() {
        override fun areItemsTheSame(oldItem: UsersBySearchResponse, newItem: UsersBySearchResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: UsersBySearchResponse, newItem: UsersBySearchResponse): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var users: List<UsersBySearchResponse>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUsersAdapter.SearchedUserViewHolder {
        return SearchedUserViewHolder(
            SearchedUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: SearchUsersAdapter.SearchedUserViewHolder, position: Int) {
        holder.binding.apply {
            val currentUser = users[position]

            username.text = currentUser.username

            if(currentUser.selectedFile.isNotEmpty()){
                userProfileImg.setImageBitmap(decodeBase64String.decodeBase64(currentUser.selectedFile))
            }

            username.setOnClickListener { v ->
                profileFragment = ProfileFragment(userId, currentUser._id, token)
                redirectToFragment.redirect(v, profileFragment)
            }

            userProfileImg.setOnClickListener { v ->
                profileFragment = ProfileFragment(userId, currentUser._id, token)
                redirectToFragment.redirect(v, profileFragment)
            }
        }
    }
}