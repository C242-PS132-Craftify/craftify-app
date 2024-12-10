package com.craftify.craftify_app.ui.savedCraft

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.databinding.FragmentResultBinding
import com.craftify.craftify_app.databinding.FragmentSavedCraftBinding
import com.craftify.craftify_app.ui.result.DetectionsAdapter
import com.craftify.craftify_app.ui.result.OnItemClickListener
import com.craftify.craftify_app.ui.result.ResultViewModel
import com.craftify.craftify_app.ui.result.ResultViewModelFactory
import com.craftify.craftify_app.utils.ViewModelFactory

class SavedCraftFragment : Fragment(), OnItemClickListener {

    private lateinit var binding : FragmentSavedCraftBinding
    private lateinit var adapter: SavedCraftAdapter

    private val resultModel: ResultViewModel by viewModels {
        ViewModelFactory(requireContext())
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
        // Inflate the layout for this fragment
        binding = FragmentSavedCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerViewRecommendation
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
        resultModel.savedCraft.observe(viewLifecycleOwner) { crafts ->
            adapter = SavedCraftAdapter(crafts, this)
            recyclerView.adapter = adapter
        }
    }

    companion object {

    }

    override fun onItemClick(title: String?) {
        val bundle = Bundle().apply {
            putString("title", title)
        }
        Log.d("ItemClick", "bundle: ${title}")

        findNavController().navigate(R.id.action_savedcraft_to_detail_craft, bundle)
    }
}