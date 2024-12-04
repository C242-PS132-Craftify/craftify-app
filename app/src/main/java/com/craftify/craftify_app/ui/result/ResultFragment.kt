package com.craftify.craftify_app.ui.result

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat.getParcelableArrayList
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.DetectionsItem
import com.craftify.craftify_app.data.server.api.RecommendationsItem
import com.craftify.craftify_app.databinding.FragmentResultBinding
import com.craftify.craftify_app.ui.scan.ScanViewModel

class ResultFragment : Fragment() {

    private lateinit var binding : FragmentResultBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            data = it.getString(ARG_PARAM1)

        }
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

        val model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(ScanViewModel::class.java)
        Log.d("ViewModelCheck", "Fragment2 ViewModel: ${model.hashCode()}")

        // Set up RecyclerView
        val recyclerView = binding.recyclerViewDetections
        val recycleViewRecommen = binding.recyclerViewRecommendation
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recycleViewRecommen.layoutManager = LinearLayoutManager(requireContext())

        // Observe LiveData
        model.detectionsList.observe(viewLifecycleOwner) { detections ->
            if (!detections.isNullOrEmpty()) {
                recyclerView.adapter = DetectionsAdapter(detections)
            } else {
                Log.d("ResultFragment", "Detections list is empty or null")
            }
        }
        // Observe LiveData
        model.recommendationsList.observe(viewLifecycleOwner) { recommen ->
            if (!recommen.isNullOrEmpty()) {
                recycleViewRecommen.adapter = RecommendationAdapter(recommen)
            } else {
                Log.d("ResultFragment", "Recommen list is empty or null")
            }
        }


    }
    
}