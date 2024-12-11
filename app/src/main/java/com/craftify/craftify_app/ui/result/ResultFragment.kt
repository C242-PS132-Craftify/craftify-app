package com.craftify.craftify_app.ui.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.databinding.FragmentResultBinding
import com.craftify.craftify_app.ui.scan.ScanViewModel
import com.craftify.craftify_app.utils.ViewModelFactory

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private val resultModel: ResultViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

    private val adapter: RecommendationAdapter by lazy { RecommendationAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("Fragment 2", "onCreateView ")
        binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView for detections
        val recyclerView = binding.recyclerViewDetections
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Set up RecyclerView for recommendations
        val recyclerViewRecommendations = binding.recyclerViewRecommendation
        recyclerViewRecommendations.layoutManager = LinearLayoutManager(requireContext())

        // Set the adapter for recommendations
        recyclerViewRecommendations.adapter = adapter

        // Set up ViewModel and observe LiveData
        val model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(ScanViewModel::class.java)
        Log.d("ViewModelCheck", "Fragment2 ViewModel: ${model.hashCode()}")

        // Observe detections
        model.detectionsList.observe(viewLifecycleOwner) { detections ->
            if (!detections.isNullOrEmpty()) {
                recyclerView.adapter = DetectionsAdapter(detections)
            } else {
                Log.d("ResultFragment", "Detections list is empty or null")
            }
        }

        // Observe recommendations
        model.recommendationsList.observe(viewLifecycleOwner) { recommendations ->
            if (!recommendations.isNullOrEmpty()) {
                // Update the RecommendationAdapter with the new recommendations
                adapter.submitList(recommendations)
            } else {
                Log.d("ResultFragment", "Recommendations list is empty or null")
            }
        }

        // Back button click listener
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.back_to_fragmentScan)
        }
    }
}
