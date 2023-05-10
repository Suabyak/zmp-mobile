package com.example.mobilesocialapp.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.SearchedUserBinding
import com.example.mobilesocialapp.fragments.ProfileFragment
import com.example.mobilesocialapp.response.UsersBySearchResponse
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.RedirectToFragment

class SearchUsersAdapter(val currentLoggedUserId: String, val token: String) : RecyclerView.Adapter<SearchUsersAdapter.SearchedUserViewHolder>() {
    private val decodeBase64String = DecodeBase64String()
    private val profileFragment = ProfileFragment()
    private val redirectToFragment = RedirectToFragment()
    private val bundleData = Bundle()

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
        bundleData.putString(BundleConsts.BundleToken, token)
        bundleData.putString(BundleConsts.BundleCurrentLoggedUserId, currentLoggedUserId)

        holder.binding.apply {
            val currentUser = users[position]

            username.text = currentUser.username

            if(currentUser.selectedFile.isNotEmpty()){
                userProfileImg.setImageBitmap(decodeBase64String.decodeBase64(currentUser.selectedFile))
            }

            username.setOnClickListener { v ->
                bundleData.putString(BundleConsts.BundleUserId, currentUser._id)
                profileFragment.arguments = bundleData
                redirectToFragment.redirect(v, profileFragment)
            }

            userProfileImg.setOnClickListener { v ->
                bundleData.putString(BundleConsts.BundleUserId, currentUser._id)
                profileFragment.arguments = bundleData
                redirectToFragment.redirect(v, profileFragment)
            }
        }
    }
}