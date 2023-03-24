package com.example.mobilesocialapp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.DecodeBase64String
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.databinding.FragmentEditPostBinding
import com.example.mobilesocialapp.request.CreatePostRequest
import com.example.mobilesocialapp.request.EditPostRequest
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException

class EditPostFragment(val postId: String) : Fragment() {
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private val decodeBase64String = DecodeBase64String()
    private lateinit var uri: Uri
    private lateinit var imageString: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getPostById(postId)
            } catch(e: IOException) {
                binding.editPostMessage.text = "You might not have internet connection"
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.editPostMessage.text = "Unexpected response"
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                binding.descriptionInput.setText(response.body()!!.message)
                binding.imageView.setImageBitmap(decodeBase64String.decodeBase64(response.body()!!.selectedFile))
                imageString = response.body()!!.selectedFile
            } else {
                binding.editPostMessage.text = "Cant retrieve this post"
            }
        }

        binding.imageButton.setOnClickListener() {
            uploadImage(binding.imageView)
        }

        binding.editPostButton.setOnClickListener {
            val descriptionInput = binding.descriptionInput.text.toString()

            if(TextUtils.isEmpty(descriptionInput)) {
                binding.descriptionInput.setError("Please enter description")
            } else {
                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newEditPostRequest = EditPostRequest(postId, descriptionInput, imageString)
                        RetrofitInstance.api.updatePost(newEditPostRequest)
                    } catch(e: IOException) {
                        binding.editPostMessage.text = "You might not have internet connection"
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.editPostMessage.text = "Unexpected response"
                        return@launchWhenCreated
                    }

                    if(response.isSuccessful && response.body() != null) {
                        binding.editPostMessage.text = "Post updated successfully"
                    } else {
                        binding.editPostMessage.text = "Post not updated! Try again"
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
        }
    }
}