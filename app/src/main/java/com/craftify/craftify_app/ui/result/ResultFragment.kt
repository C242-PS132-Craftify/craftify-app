package com.craftify.craftify_app.ui.result

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.local.room.RecommendationDatabase
import com.craftify.craftify_app.data.repository.SavedCraftRepository
import com.craftify.craftify_app.databinding.FragmentResultBinding
import com.craftify.craftify_app.ui.scan.ScanViewModel
import com.craftify.craftify_app.utils.ViewModelFactory

class ResultFragment : Fragment(), OnItemClickListener, OnCraftSaveListener {

    private lateinit var binding : FragmentResultBinding
    private val resultModel: ResultViewModel by viewModels {
        ViewModelFactory(requireActivity())
    }

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

        val model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(ScanViewModel::class.java)
        Log.d("ViewModelCheck", "Fragment2 ViewModel: ${model.hashCode()}")

        // Set up RecyclerView
        val recyclerView = binding.recyclerViewDetections
        val recycleViewRecommen = binding.recyclerViewRecommendation
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recycleViewRecommen.layoutManager = LinearLayoutManager(requireContext())

        //loading data

        // Observe LiveData
        model.detectionsList.observe(viewLifecycleOwner) { detections ->
            if (!detections.isNullOrEmpty()) {
                when(detections){}
                recyclerView.adapter = DetectionsAdapter(detections)
            } else {
                Log.d("ResultFragment", "Detections list is empty or null")
            }
        }
        // Observe LiveData
        model.recommendationsList.observe(viewLifecycleOwner) { recommen ->
            if (!recommen.isNullOrEmpty()) {
                recycleViewRecommen.adapter = RecommendationAdapter(recommen, this, this )
            } else {
                Log.d("ResultFragment", "Recommen list is empty or null")
            }
        }

        //button back
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.back_to_fragmentScan)
        }


    }

    override fun onItemClick(title: String?) {
        val bundle = Bundle().apply {
            putString("title", title) // Ensure your data class implements Parcelable
        }
        Log.d("ItemClick", "bundle: ${title}")

        findNavController().navigate(R.id.action_to_fragmentCraftDetail, bundle)
    }

    override fun onCraftSave(title: String?, image: String?) {
        if (!title.isNullOrEmpty() && !image.isNullOrEmpty()) {
            resultModel.saveCraft(title, image)
            Toast.makeText(context, "Item Saved", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("onCraftSave", "Invalid data. Title or image is missing.")
        }
    }


}