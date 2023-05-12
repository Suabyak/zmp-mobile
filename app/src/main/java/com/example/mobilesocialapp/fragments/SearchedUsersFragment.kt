package com.example.mobilesocialapp.fragments

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobilesocialapp.R
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.adapters.PostAdapter
import com.example.mobilesocialapp.adapters.SearchUsersAdapter
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentSearchedUsersBinding
import retrofit2.HttpException
import java.io.IOException

class SearchedUsersFragment : Fragment() {
    private var _binding: FragmentSearchedUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchUsersAdapter: SearchUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchedUsersBinding.inflate(inflater, container, false)

        val data = arguments
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()
        val token = data?.get(BundleConsts.BundleToken).toString()

        setupRecyclerView(currentLoggedUserId, token)

        binding.searchBtn.setOnClickListener {
            val searchValue = binding.usernameText.text.toString()

            if(TextUtils.isEmpty(searchValue)){
                binding.username.error = "Please enter username"
            } else {
                lifecycleScope.launchWhenCreated {
                    val response = try {
                        RetrofitInstance.api.getUsersBySearch(searchValue)
                    } catch(e: IOException) {
                        binding.searchedUsersMessage.text = BadResponses.notInternetConnection
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.searchedUsersMessage.text = BadResponses.unexpectedResponse
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        searchUsersAdapter.users = response.body()!!
                        binding.searchedUsersMessage.text = ""

                        if (searchUsersAdapter.itemCount == 0) {
                            binding.searchedUsersMessage.text = "No users found"
                        } else {
                            binding.searchedUsersMessage.text = ""
                        }
                    } else {
                        binding.searchedUsersMessage.text = "Cant retrieve users"
                    }
                }
            }
        }

        return binding.root
    }

    private fun setupRecyclerView(currentLoggedUserId: String, token: String) = binding.rvSearchUsers.apply {
        searchUsersAdapter = SearchUsersAdapter(currentLoggedUserId, token)
        adapter = searchUsersAdapter
        layoutManager = LinearLayoutManager(this@SearchedUsersFragment.context)
    }

}