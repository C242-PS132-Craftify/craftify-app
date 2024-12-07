package com.craftify.craftify_app.ui.blog

import CreatedAt
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.databinding.FragmentBlogDetailsBinding
import com.craftify.craftify_app.utils.ViewModelFactory

class BlogDetailsFragment : Fragment() {

    private var _binding : FragmentBlogDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : BlogViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlogDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args : BlogDetailsFragmentArgs by navArgs()
        val blogId = args.blogId

        viewModel.fetchBlogDetails(blogId)

        observeBlogDetails()
    }

    private fun observeBlogDetails() {
        viewModel.blogDetails.observe(viewLifecycleOwner) {result->
            when (result) {
                is Result.Success ->{
                    val blog = result.data
                    val date = formatCreatedAt(blog.createdAt)
                    binding.tvTitle.text = blog.title
                    binding.tvAuthor.text = blog.author
                    binding.tvContent.text = blog.content
                    binding.tvDate.text = date
                    Glide.with(binding.ivHeaderImage.context)
                        .load(blog.headerImage)
                        .placeholder(R.drawable.ic_placeholder_image)
                        .into(binding.ivHeaderImage)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), result.error,Toast.LENGTH_LONG ).show()
                }
                Result.Loading -> {

                }
            }
        }
    }

    private fun formatCreatedAt(createdAt: CreatedAt?): String {
        return createdAt?.let {
            val seconds = it.seconds ?: return@let "Unknown date"
            val date = java.util.Date(seconds * 1000L)
            val dateFormat = java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
            dateFormat.format(date)
        } ?: "Unknown date"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}