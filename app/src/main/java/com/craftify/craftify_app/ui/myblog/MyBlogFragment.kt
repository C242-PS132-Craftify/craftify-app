package com.craftify.craftify_app.ui.myblog

import CreatedAt
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.model.Blog
import com.craftify.craftify_app.databinding.FragmentMyBlogBinding
import com.craftify.craftify_app.ui.blog.AddEditBlogActivity
import com.craftify.craftify_app.ui.blog.BlogDetailsActivity
import com.craftify.craftify_app.utils.CustomLoadingDialog
import com.craftify.craftify_app.utils.ViewModelFactory

class MyBlogFragment : Fragment() {

    private var _binding : FragmentMyBlogBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: MyBlogAdapter
    private val viewModel : MyBlogViewModel by viewModels { ViewModelFactory(requireContext()) }

    private lateinit var loadingDialog: CustomLoadingDialog

    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingDialog = CustomLoadingDialog(requireContext())

        adapter = MyBlogAdapter(requireContext(), onEdit = { blog ->
            val intent = Intent(requireContext(), AddEditBlogActivity::class.java)
            intent.putExtra("BLOG_ID", blog.id)
            startActivity(intent)
        }, onDelete = { blog ->
            showDeleteConfirmationDialog(blog)
        })

        binding.rvBlogs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBlogs.adapter = adapter

        observeBlogs()
        setupListeners()

        viewModel.fetchMyBlogs()
    }

    private fun showDeleteConfirmationDialog(blog: Blog) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_confirmation, null)
        //val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val tvBlogTitle = dialogView.findViewById<TextView>(R.id.tvBlogTitle)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        tvBlogTitle.text = blog.title

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            viewModel.deleteBlog(blog.id)
            Toast.makeText(requireContext(), "Blog deleted successfully!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchMyBlogs()
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeBlogs() {
        viewModel.blogs.observe(viewLifecycleOwner){result->
            when (result) {
                is Result.Loading -> {
                    if (count == 0){
                        loadingDialog.show()
                        count++
                        binding.swipeRefreshLayout.isRefreshing = false
                    } else {
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    loadingDialog.dismiss()
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
                    loadingDialog.dismiss()
                    show404Error()
                }
            }
        }
    }

    @SuppressLint("InflateParams")
    private fun show404Error() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.not_found, null)

        val errorMessageTextView: TextView = view.findViewById(R.id.tvErrorMessage)

        errorMessageTextView.text = buildString {
        append("No blogs found." + "\n" +
                "Come on, share your craft!")
        }

        val addBlogButton: Button = view.findViewById(R.id.btnAddBlog)

        addBlogButton.setOnClickListener {
            val intent = Intent(requireContext(), AddEditBlogActivity::class.java)
            startActivity(intent)
        }

        val swipeRefreshLayout = androidx.swiperefreshlayout.widget.SwipeRefreshLayout(requireContext()).apply {
            addView(view)
            setOnRefreshListener {
                isRefreshing = false
                viewModel.fetchMyBlogs()
            }
        }

        val container: ConstraintLayout = binding.myBlog
        container.removeAllViews()
        container.addView(swipeRefreshLayout)
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