package com.craftify.craftify_app.ui.blog

import CreatedAt
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.model.Blog
import com.craftify.craftify_app.databinding.FragmentBlogBinding
import com.craftify.craftify_app.utils.ViewModelFactory

class BlogFragment : Fragment() {

    private var _binding : FragmentBlogBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BlogAdapter

    private val viewModel : BlogViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BlogAdapter(requireContext())
        binding.rvBlogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBlogs.adapter = adapter

        observeBlogs()
        setupListeners()

        viewModel.fetchAllBlogs()
    }

    private fun setupListeners() {
        binding.fabAdd.setOnClickListener{
            val intent = Intent(requireContext(), AddEditBlogActivity::class.java)
            startActivity(intent)
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchAllBlogs()
        }
    }

    private fun observeBlogs() {
        viewModel.blogs.observe(viewLifecycleOwner){result->
            when (result) {
                is Result.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    val data = result.data.map { blog ->
                        Blog(
                            id = blog.id ?: "",
                            title = blog.title ?: "Untitled",
                            author = blog.author ?: "Unknown",
                            content = blog.content ?: "No content",
                            headerImage = blog.headerImage ?: "",
                            createdAt = formatCreatedAt(blog.createdAt)
                        )
                    }
                    adapter.submitList(data)
                }
                is Result.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}