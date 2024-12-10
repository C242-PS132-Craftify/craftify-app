package com.craftify.craftify_app.ui.blog

import CreatedAt
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.databinding.ActivityBlogDetailsBinding
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.utils.CustomLoadingDialog
import com.craftify.craftify_app.utils.ViewModelFactory

class BlogDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBlogDetailsBinding

    private val viewModel : BlogViewModel by viewModels { ViewModelFactory(this)}

    private lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlogDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = CustomLoadingDialog(this)

        val blogId = intent.getStringExtra("BLOG_ID")

        blogId?.let {
            viewModel.fetchBlogDetails(it)
            observeBlogDetails()
        } ?: run {
            Toast.makeText(this, "Blog ID is missing", Toast.LENGTH_LONG).show()
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeBlogDetails() {
        viewModel.blogDetails.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    loadingDialog.dismiss()
                    val blog = result.data
                    val date = formatCreatedAt(blog.createdAt)
                    binding.tvTitle.text = blog.title
                    binding.tvAuthor.text = buildString {
                        append("Author : ")
                        append(blog.author)
                    }
                    binding.tvContent.text = blog.content
                    binding.tvDate.text = buildString {
                        append("published at : ")
                        append(date)
                    }
                    Glide.with(binding.ivHeaderImage.context)
                        .load(blog.headerImage)
                        .placeholder(R.drawable.ic_logo)
                        .into(binding.ivHeaderImage)
                }
                is Result.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, result.error, Toast.LENGTH_LONG).show()
                }
                Result.Loading -> {
                    loadingDialog.show()
                }
            }
        }
    }

    private fun formatCreatedAt(createdAt: CreatedAt?): String {
        return createdAt?.let {
            val seconds = it.seconds ?: return@let "Unknown date"
            val date = java.util.Date(seconds * 1000L)
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            dateFormat.format(date)
        } ?: "Unknown date"
    }
}