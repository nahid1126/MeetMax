package com.nahid.meetmax.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nahid.meetmax.R
import com.nahid.meetmax.databinding.FragmentCreatePostBinding
import com.nahid.meetmax.utils.AppPreferences
import com.nahid.meetmax.utils.ApplicationCallBack
import com.nahid.meetmax.utils.CustomToast
import com.nahid.meetmax.utils.ImageUtils
import com.nahid.meetmax.utils.Status
import com.nahid.meetmax.utils.checkStoragePermission
import com.nahid.meetmax.view_models.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.InputStream
import javax.inject.Inject

@AndroidEntryPoint
class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var mainListener: ApplicationCallBack.MainListener


    @Inject
    lateinit var appPreferences: AppPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostBinding.inflate(layoutInflater)

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this

        appPreferences.getGetUserEmail()?.let {
            userViewModel.setUser(it)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                userViewModel.message.collect {
                    CustomToast.showToast(requireContext(), it, Status.FAILED)
                }
            }
        }
        binding.textCreatePost.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_dashboardFragment)
        }

        binding.buttonPost.setOnClickListener {
            userViewModel.addPost { result, message ->
                if (result) {
                    CustomToast.showToast(requireContext(), message, Status.SUCCESS)
                } else {
                    CustomToast.showToast(requireContext(), message, Status.FAILED)
                }
            }
        }

        binding.photoVideo.setOnClickListener {
            if (requireContext().checkStoragePermission()) {
                galleryContractFirst.launch("image/*")
            } else {
                mainListener.requestStoragePermission()
            }
        }

        return binding.root
    }
    private val galleryContractFirst = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri ->
            val imageStream: InputStream? = context?.contentResolver?.openInputStream(imageUri)
            val selectedImageBitMap: Bitmap = BitmapFactory.decodeStream(imageStream)
            val croppedImageBitmap = ImageUtils.scaleCenterCrop(selectedImageBitMap, 400, 400)
            val compressedImage = ImageUtils.reduceImageSizeFromBitmapToBase64(croppedImageBitmap, true)
            userViewModel.setPostImageImage(compressedImage)
            binding.imageUpload.visibility = View.VISIBLE
            binding.imageUpload.setImageBitmap(croppedImageBitmap)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ApplicationCallBack.MainListener) {
            mainListener = context
        }
    }
}