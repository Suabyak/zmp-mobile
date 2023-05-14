package com.example.mobilesocialapp.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import com.example.mobilesocialapp.R
import com.example.mobilesocialapp.RetrofitInstance
import com.example.mobilesocialapp.constants.BadResponses
import com.example.mobilesocialapp.constants.BundleConsts
import com.example.mobilesocialapp.databinding.FragmentChangeProfileBinding
import com.example.mobilesocialapp.request.ChangeProfileRequest
import com.example.mobilesocialapp.utils.DecodeBase64String
import com.example.mobilesocialapp.utils.RedirectToFragment
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import java.io.IOException

class ChangeProfileFragment : Fragment() {
    private var _binding: FragmentChangeProfileBinding? = null
    private val binding get() = _binding!!
    private var imageString: String = ""
    private lateinit var uri: Uri
    private val decodeBase64String = DecodeBase64String()
    private val profileFragment = ProfileFragment()
    private val redirectToFragment = RedirectToFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeProfileBinding.inflate(inflater, container, false)

        val data = arguments
        val token = data?.get(BundleConsts.BundleToken).toString()
        val userId = data?.get(BundleConsts.BundleUserId).toString()
        val currentLoggedUserId = data?.get(BundleConsts.BundleCurrentLoggedUserId).toString()

        val bundleData = Bundle()
        bundleData.putString(BundleConsts.BundleToken, token)
        bundleData.putString(BundleConsts.BundleUserId, userId)
        bundleData.putString(BundleConsts.BundleCurrentLoggedUserId, currentLoggedUserId)

        binding.setImg.setOnClickListener {
            uploadImage(binding.userProfileImg)
        }

        lifecycleScope.launchWhenCreated {
            val response = try {
                RetrofitInstance.api.getUserDataProfile(userId)
            } catch(e: IOException) {
                binding.errorMessage.text = BadResponses.notInternetConnection
                return@launchWhenCreated
            } catch(e: HttpException) {
                binding.errorMessage.text = BadResponses.unexpectedResponse
                return@launchWhenCreated
            }

            if(response.isSuccessful && response.body() != null) {
                if(response.body()!!.file.isNotEmpty()) {
                    binding.userProfileImg.setImageBitmap(decodeBase64String.decodeBase64(response.body()!!.file))
                    imageString = response.body()!!.file
                }
            } else {
                binding.errorMessage.text = "Cant retrieve user image"
            }
        }

        binding.changeProfile.setOnClickListener {
            if (imageString.isEmpty()) {
                binding.errorMessage.text = "Select profile image"
            } else {
                binding.errorMessage.text = ""
                binding.progressBar.visibility = View.VISIBLE

                lifecycleScope.launchWhenCreated {
                    val response = try {
                        val newChangeProfileRequest = ChangeProfileRequest(imageString)
                        RetrofitInstance.api.changeProfile(newChangeProfileRequest, "Bearer $token")
                    } catch(e: IOException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorMessage.text = BadResponses.notInternetConnection
                        return@launchWhenCreated
                    } catch(e: HttpException) {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.errorMessage.text = BadResponses.unexpectedResponse
                        return@launchWhenCreated
                    }

                    if (response.isSuccessful && response.body() != null) {
                        binding.progressBar.visibility = View.INVISIBLE
                        profileFragment.arguments = bundleData
                        redirectToFragment.redirect(binding.root, profileFragment)
                    } else {
                        binding.errorMessage.text = response.body()!!.message
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
            binding.userProfileImg.setImageURI(data?.data)

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