package com.example.mobilesocialapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.DashboardActivity
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.databinding.FragmentCreateBinding
import com.example.mobilesocialapp.request.AuthRequest
import com.example.mobilesocialapp.request.CreatePostRequest
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException


class CreateFragment : Fragment() {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var uri: Uri
    private lateinit var imageString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)

        val data = arguments
        val token = data?.get("token").toString()

        binding.imageButton.setOnClickListener() {
            uploadImage(binding.imageView)
        }

        binding.createPostButton.setOnClickListener {
            val descriptionInput = binding.descriptionInput.text.toString()

            if(TextUtils.isEmpty(descriptionInput)) {
                binding.descriptionInput.setError("Please enter description")
            } else {
                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newCreatePostRequest = CreatePostRequest(descriptionInput, imageString)
                        RetrofitInstance.api.createPost(newCreatePostRequest, "Bearer $token")
                    } catch(e: IOException) {
                        binding.createPostMessage.text = "You might not have internet connection"
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.createPostMessage.text = "Unexpected response"
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        binding.createPostMessage.text = "Post created successfully"
                    } else {
                        binding.createPostMessage.text = "Post not created! Try again"
                    }
                }
            }
        }

        return binding.root
    }

    private fun uploadImage(image: ImageView?) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            binding.imageView.setImageURI(data?.data)

            uri = data?.data!!
            val input = activity?.contentResolver?.openInputStream(uri)
            val image = BitmapFactory.decodeStream(input, null, null)

            // Encode image to base64 string
            val baos = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            var imageBytes = baos.toByteArray()
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            // Decode base64 string to image
//            val imageBytesToDecode = Base64.decode(imageString, Base64.DEFAULT)
//            val decodedImage = BitmapFactory.decodeByteArray(imageBytesToDecode, 0, imageBytes.size)
//
//            binding.imageView.setImageBitmap(decodedImage)
        }
    }
}