package com.craftify.craftify_app.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.data.model.Blog
import com.craftify.craftify_app.databinding.FragmentProfileBinding
import com.craftify.craftify_app.ui.about.AboutActivity
import com.craftify.craftify_app.ui.login.LoginActivity
import com.craftify.craftify_app.utils.ViewModelFactory


class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ProfileViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUser()
        setupListeners()
        observeViewModel()

        binding.btnSave.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_savedFragment)
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getUser()
        }
    }

    private fun observeViewModel() {
        viewModel.currentUser.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is Result.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.tvEmail.text = result.data.email
                    binding.tvUsername.text = result.data.username
                    Glide.with(binding.imgProfile.context)
                        .load(result.data.profile_picture)
                        .placeholder(R.drawable.login_image)
                        .into(binding.imgProfile)
                }
                is Result.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                    Log.d("Error", result.error)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.button5.setOnClickListener{
            showLogoutConfirmationDialog()
        }
        binding.btnBlog.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_myBlogFragment)
        }
        binding.btnAboutus.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }
        binding.cardProfile.setOnClickListener{
            val intent = Intent(requireContext(), AccountInformationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLogoutConfirmationDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_confirmation, null)
        val tvMessage = dialogView.findViewById<TextView>(R.id.tvMessage)
        val tvBlogTitle = dialogView.findViewById<TextView>(R.id.tvBlogTitle)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)

        tvMessage.text = buildString {
            append("Are you sure want to logout?")
        }

        tvBlogTitle.text = buildString {
            append("All Saved Craft Recipe Will Be Deleted!")
        }

        btnDelete.text = buildString {
            append("Log Out")
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            viewModel.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}