package com.craftify.craftify_app.ui.scan

import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.ApiConfig
import com.craftify.craftify_app.data.server.response.PredictResponse
import com.craftify.craftify_app.databinding.FragmentScanBinding
import com.craftify.craftify_app.utils.CustomLoadingDialog

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ScanFragment : Fragment() {

    private lateinit var binding: FragmentScanBinding
    private var imageCapture: ImageCapture? = null
    private var capturedImagePath: String? = null

    private lateinit var loadingDialog: CustomLoadingDialog

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission request accepted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val filePath = getPathFromUri(it)
            if (filePath != null) {
                // Use the filePath for uploading
                Toast.makeText(requireContext(), "File Path: $filePath", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Failed to retrieve file path", Toast.LENGTH_SHORT).show()
            }
            capturedImagePath = filePath
            Glide.with(this)
                .load(it)
                .into(binding.capturedImageView)

            binding.capturedImageView.visibility = View.VISIBLE
        } ?: run {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use ContentResolver to copy the file to a temporary location
            val file = File(requireContext().cacheDir, "temp_image.jpg")
            requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            file.absolutePath
        } else {
            // For older versions, use the legacy approach
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireContext().contentResolver.query(uri, projection, null, null, null)
            cursor?.use {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                if (it.moveToFirst()) {
                    it.getString(columnIndex)
                } else null
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        binding.btnCapture.setOnClickListener { takePhoto() }
        binding.btnPickImage.setOnClickListener {
            pickImageFromGallery()
        }
        binding.btnAnalyze.setOnClickListener {
            // Replace `filePath` with the actual path of the image file you want to upload
            val filePath = capturedImagePath // Or handle the selected image path
            if (filePath != null) {
                uploadImage(filePath)
            } else {
                Toast.makeText(requireContext(), "No image to upload!", Toast.LENGTH_SHORT).show()
            }
        }
        loadingDialog = CustomLoadingDialog(requireContext())
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (checkPermissions()) {
            startCamera()
        } else {
            Toast.makeText(requireContext(), "Camera permission rejected", Toast.LENGTH_SHORT).show()
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder()
                .setTargetRotation(requireView().display.rotation)
                .build()

            val cameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(requireContext().cacheDir, "captured_image_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(requireContext(), "Photo capture failed: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    capturedImagePath = photoFile.absolutePath
                    Toast.makeText(requireContext(), "Photo captured successfully", Toast.LENGTH_SHORT).show()
                    Glide.with(this@ScanFragment).load(photoFile).into(binding.capturedImageView)
                    binding.capturedImageView.visibility = View.VISIBLE
                    binding.previewView.visibility = View.GONE
                }
            }
        )
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun pickImageFromGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun uploadImage(filePath: String) {
        val file = File(filePath)
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val apiService = ApiConfig.getApiService()
        val call = apiService.uploadImage(body)

        val model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(ScanViewModel::class.java)
        Log.d("ViewModelCheck", "Fragment1 ViewModel: ${model.hashCode()}")

        // Show the ProgressBar
        val progressBar = binding.progressBar // Assuming you're using ViewBinding
        loadingDialog.show()

        call.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                if (response.isSuccessful) {
                    loadingDialog.dismiss()
                    Log.d(TAG, "onResponse: success \n $response")
                    val detections = response.body()?.data?.detections?.filterNotNull()
                    val recommendations = response.body()?.data?.recommendations?.filterNotNull()
                    Log.d("Fragment1", "Detections: $detections, Recommendations: $recommendations")

                    model.setDetectionsList(detections)
                    model.setRecommendationsList(recommendations)
                    findNavController().navigate(R.id.action_to_fragmentReuslt)
                } else {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), "Failed to Scan Object", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "onResponse: failed \n $response")
                    progressBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                loadingDialog.dismiss()
                Log.d("Errrormessage", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Failed to Scan Object", Toast.LENGTH_SHORT).show()
            }
        })
    }



}

