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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.databinding.FragmentResultBinding
import com.craftify.craftify_app.databinding.FragmentSavedCraftBinding
import com.craftify.craftify_app.ui.result.DetectionsAdapter
import com.craftify.craftify_app.ui.result.OnItemClickListener
import com.craftify.craftify_app.ui.result.ResultViewModel
import com.craftify.craftify_app.ui.result.ResultViewModelFactory
import com.craftify.craftify_app.utils.ViewModelFactory

class SavedCraftFragment : Fragment() {

    private var _binding : FragmentSavedCraftBinding? = null
    private val binding get() = _binding!!
    private val adapter: SavedCraftAdapter by lazy { SavedCraftAdapter() }

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
        _binding = FragmentSavedCraftBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupRecyclerView()

        resultModel.getSavedCraft.observe(viewLifecycleOwner) {craft->
            val listCrafts = craft.map { el->
                RecommendationsItem(
                    projectImg = el.projectImg,
                    projectName = el.projectName
                )
            }
            adapter.submitList(listCrafts)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRecommendation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SavedCraftFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}