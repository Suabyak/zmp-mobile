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
        val userId = data?.get("userId").toString()
        val token = data?.get("token").toString()

        setupRecyclerView(userId, token)

        binding.searchBtn.setOnClickListener {
            val searchValue = binding.usernameText.text.toString()
            if(TextUtils.isEmpty(searchValue)){
                binding.username.error = "Please enter username"
            } else {
                lifecycleScope.launchWhenCreated {
                    val response = try {
                        RetrofitInstance.api.getUsersBySearch(searchValue)
                    } catch(e: IOException) {
                        binding.errorMessage.text = "You might not have internet connection"
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.errorMessage.text = "Unexpected response"
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        searchUsersAdapter.users = response.body()!!
                        binding.errorMessage.text = ""
                    } else {
                        binding.errorMessage.text = "Cant retrieve users"
                    }
                }
            }
        }

        return binding.root
    }

    private fun setupRecyclerView(userId: String, token: String) = binding.rvSearchUsers.apply {
        searchUsersAdapter = SearchUsersAdapter(userId, token)
        adapter = searchUsersAdapter
        layoutManager = LinearLayoutManager(this@SearchedUsersFragment.context)
    }

}