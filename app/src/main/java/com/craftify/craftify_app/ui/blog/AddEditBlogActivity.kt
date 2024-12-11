package com.craftify.craftify_app.ui.blog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.craftify.craftify_app.databinding.ActivityAddEditBlogBinding
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.utils.ViewModelFactory
import java.io.File
import java.io.IOException
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.utils.CreateMultipartBody
import com.craftify.craftify_app.utils.CustomLoadingDialog


class AddEditBlogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditBlogBinding
    private val blogViewModel: BlogViewModel by viewModels { ViewModelFactory(this) }

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null
    private var uploadedImageUrl: String? = null

    private lateinit var loadingDialog: CustomLoadingDialog

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
            Glide.with(this)
                .load(uri)
                .placeholder(R.drawable.ic_logo)
                .into(binding.ivHeaderImage)
            uploadedImageUrl = null
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showBackConfirmationDialog()
            }
        })

        loadingDialog = CustomLoadingDialog(this)

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                binding.ivHeaderImage.setImageURI(imageUri)
                uploadedImageUrl = null
            }
        }

        if (intent.hasExtra("BLOG_ID")) {
            val blogId = intent.getStringExtra("BLOG_ID")!!
            blogViewModel.fetchBlogDetails(blogId)

            blogViewModel.blogDetails.observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                    }
                    is Result.Success -> {
                        loadingDialog.dismiss()
                        val blog = result.data
                        binding.etTitle.setText(blog.title)
                        binding.etContent.setText(blog.content)
                        if (blog.headerImage?.isNotEmpty() == true) {
                            imageUri = Uri.parse(blog.headerImage)
                            Glide.with(binding.ivHeaderImage.context)
                                .load(blog.headerImage)
                                .placeholder(R.drawable.ic_logo)
                                .into(binding.ivHeaderImage)
                        }
                        uploadedImageUrl = blog.headerImage
                    }
                    is Result.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(this, "Error fetching blog details: ${result.error}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            val toolbar = binding.topAppBar
            toolbar.title = "EDIT BLOG"
            setSupportActionBar(toolbar)
        } else {
            val toolbar = binding.topAppBar
            toolbar.title = "POST BLOG"
            setSupportActionBar(toolbar)
        }

        binding.btnUploadHeaderImage.setOnClickListener {
            showImagePickerDialog()
        }

        binding.topAppBar.setNavigationOnClickListener {
            showBackConfirmationDialog()
        }

        blogViewModel.uploadImage.observe(this) { result ->
            when (result) {
                is Result.Loading -> {

                }

                is Result.Success -> {
                    loadingDialog.dismiss()
                    uploadedImageUrl = result.data.url
                    Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                    proceedToSaveBlog()
                }

                is Result.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "Image upload failed: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        blogViewModel.addBlog.observe(this) { handleBlogResult(it, "Blog added successfully!") }
        blogViewModel.editBlog.observe(this) { handleBlogResult(it, "Blog updated successfully!") }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val content = binding.etContent.text.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Title and content are required.", Toast.LENGTH_SHORT).show()
            } else if (uploadedImageUrl == null && imageUri != null) {
                uploadHeaderImage()
            } else {
                proceedToSaveBlog()
            }
        }
    }

    private fun showImagePickerDialog() {
        val options = arrayOf("Choose from Gallery", "Take a Photo")
        AlertDialog.Builder(this)
            .setTitle("Select Image")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openGallery() // Open Gallery
                    1 -> checkCameraPermissions() // Take Photo
                }
            }
            .show()
    }

    private fun openGallery() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun checkCameraPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                takePhoto()
            } else {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } else {
            takePhoto()
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
                blogViewModel.uploadBlogImage(imagePart)
            } else {
                proceedToSaveBlog()
            }
        } ?: run {
            Toast.makeText(this, "No image selected!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun proceedToSaveBlog() {
        val title = binding.etTitle.text.toString()
        val content = binding.etContent.text.toString()
        val imageUrl = uploadedImageUrl
        var author : String? = null
        var uId : String? = null

        blogViewModel.currentUserId.observe(this){userId->
            uId = userId
        }
        blogViewModel.currentUser.observe(this){result->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    author = result.data.username
                }
                is Result.Error -> {
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    Log.d("Error", result.error)
                }
            }
        }

        Log.d("AddBlog", "Title: $title, Author: $author, UserId: $uId, Content: $content, HeaderImage: $imageUrl")



        if (intent.hasExtra("BLOG_ID")) {
            val blogId = intent.getStringExtra("BLOG_ID")!!
            blogViewModel.editBlog(blogId, title, author.toString(), uId.toString(), content, imageUrl.toString())
        } else {
            blogViewModel.addBlog(title, author.toString(), uId.toString(), content, imageUrl.toString())
        }
    }

    private fun handleBlogResult(result: Result<*>, successMessage: String) {
        when (result) {
            is Result.Loading -> {
                loadingDialog.show()
            }
            is Result.Success -> {
                loadingDialog.dismiss()
                finish()
            }
            is Result.Error -> {
                loadingDialog.dismiss()
                Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showBackConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Discard Changes?")
            .setMessage("Are you sure you want to leave? Any unsaved changes will be lost.")
            .setPositiveButton("Leave") { _, _ ->
                finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}



