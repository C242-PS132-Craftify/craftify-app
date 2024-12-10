package com.craftify.craftify_app.ui.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.databinding.ActivityAccountInformationBinding
import com.craftify.craftify_app.utils.CreateMultipartBody
import com.craftify.craftify_app.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.io.IOException

class AccountInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountInformationBinding

    private val viewModel: ProfileViewModel by viewModels { ViewModelFactory(this) }

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private var uploadedImageUrl: String? = null


    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            takePhoto()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
            imageUri = uri
            uploadHeaderImage()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {isSuccess ->
            if(isSuccess){
                uploadHeaderImage()
            }
        }
        viewModel.getUser()
        setupListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.currentUser.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.etUsername.setText(result.data.username)
                    binding.etEmail.setText(result.data.email)
                    Glide.with(binding.imgProfile.context)
                        .load(result.data.profile_picture)
                        .placeholder(R.drawable.login_image)
                        .into(binding.imgProfile)
                }
                is Result.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.uploadImage.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show()
                }
                is Result.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, "Failed to upload image: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.updateProfilePicture.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.etUsername.setText(result.data.username)
                    binding.etEmail.setText(result.data.email)
                    Glide.with(binding.imgProfile.context)
                        .load(result.data.profile_picture)
                        .placeholder(R.drawable.login_image)
                        .into(binding.imgProfile)
                }
                is Result.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(this, "Failed to update profile picture: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.imgProfile.setOnClickListener {
            checkCameraPermissions()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun checkCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                showImageDialog()
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } else {
            showImageDialog()
        }
    }

    private fun takePhoto() {
        val photoFile: File? = try {
            val storageDir = getExternalFilesDir(null)
            File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", storageDir)
        } catch (e: IOException) {
            null
        }

        photoFile?.let {
            imageUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", it)
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            cameraLauncher.launch(imageUri)
        }
    }

    private fun uploadHeaderImage() {
        imageUri?.let { uri ->
            if (uri.toString() != uploadedImageUrl) {
                val imagePart = CreateMultipartBody.fromUri(this, uri)
                viewModel.uploadAndSetProfilePicture(imagePart)
            }
        } ?: run {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showImageDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Change Profile Picture")
            .setItems(arrayOf("Take Photo", "Choose from Gallery")) { _, which ->
                when (which) {
                    0 -> takePhoto()
                    1 -> openGallery()
                }
            }
            .show()
    }
}
