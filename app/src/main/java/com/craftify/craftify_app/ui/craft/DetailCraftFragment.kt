package com.craftify.craftify_app.ui.craft

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.ApiConfig
import com.craftify.craftify_app.data.server.response.DetailResponse
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.databinding.FragmentDetailCraftBinding
import com.craftify.craftify_app.databinding.FragmentScanBinding
import com.craftify.craftify_app.utils.ViewModelFactory
import kotlinx.coroutines.launch
import com.craftify.craftify_app.data.Result
import retrofit2.Callback
import retrofit2.Response


class DetailCraftFragment : Fragment() {

    private lateinit var binding: FragmentDetailCraftBinding
    private var selectedItem: RecommendationsItem? = null

    private val viewModel : DetailCraftViewModel by viewModels { ViewModelFactory(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var title = arguments?.getString("title")

        viewModel.getCraftByName(title!!).observe(viewLifecycleOwner) {result->
            when (result){
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Error : ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    showLoading(false)
                    val data = result.data
                    Glide.with(binding.root.context)
                        .load(data.projectImg)
                        .placeholder(R.drawable.ic_logo)
                        .into(binding.ivCoverImg)

                    binding.tvTitle.text = data.projectName
                    binding.tvItemMaterial.text = data.projectMaterials?.joinToString("\n")
                    binding.tvItemSteps.text = data.projectRecipe?.joinToString("\n")
                    var isFavorite = data.isFavorite
                    if (isFavorite){
                      binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                    } else {
                        binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                    }
                    binding.fabFavorite.setOnClickListener {
                        lifecycleScope.launch {

                            isFavorite = !isFavorite
                            viewModel.toggleFavorite(title, isFavorite)
                            if (isFavorite) {
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                                Toast.makeText(requireContext(),"Recipe is saved to favorite", Toast.LENGTH_SHORT).show()
                            } else {
                                binding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                                Toast.makeText(requireContext(),"Recipe removed to favorite", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading == true) View.VISIBLE else View.GONE
    }
}