package com.craftify.craftify_app.ui.craft

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.ApiConfig
import com.craftify.craftify_app.data.server.response.DetailResponse
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.databinding.FragmentDetailCraftBinding
import com.craftify.craftify_app.databinding.FragmentScanBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailCraftFragment : Fragment() {

    private lateinit var binding: FragmentDetailCraftBinding
    private var selectedItem: RecommendationsItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailCraftBinding.inflate(inflater, container, false)
        //Retrieve passed data
        var title = arguments?.getString("title")
        getDetail(title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //button back
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }



    }

    private fun getDetail(title : String?){
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService()
        apiService.getProject(title.toString()).enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                val craft = response.body()?.data
                craft.let { list ->
                    val dataItem = list?.getOrNull(0)

                    if (dataItem != null) {
                        binding.apply {
                            Glide.with(binding.root.context)
                                  .load(dataItem.projectImg)
                                  .into(binding.ivCoverImg)

                            binding.tvTitle.setText(dataItem.projectName)

                            val materialsText = dataItem.projectMaterials?.filterNotNull()?.joinToString("\n") { it }
                                ?: "No materials available"
                            binding.tvItemMaterial.text = materialsText

                            val stepText = dataItem.projectRecipe?.filterNotNull()?.joinToString("\n") { it }
                                ?: "No Step available"
                            binding.tvItemSteps.text = stepText

                        }
                    } else {
                        binding.apply {
                            binding.tvTitle.text = "No Title"
                            binding.tvItemMaterial.text = "No Materials"
                            binding.tvItemSteps.text = "No Steps"
                        }
                        }
                }
            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                // Handle failure scenario
                binding.progressBar.visibility = View.GONE
                Log.d("DetailFragmentError", "onFailure: $t")

            }

        })
    }
}