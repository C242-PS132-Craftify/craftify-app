package com.craftify.craftify_app.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.ApiConfig
import com.craftify.craftify_app.data.server.response.DataItem
import com.craftify.craftify_app.data.server.response.DetailResponse
import com.craftify.craftify_app.databinding.FragmentHomeBinding
import com.craftify.craftify_app.ui.result.OnItemClickListener
import com.craftify.craftify_app.ui.scan.ScanViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeAdapter : HomeAdapter
    private lateinit var model: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.carousel_recycler_view) // Replace with your RecyclerView ID
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        model = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        // Observe LiveData from ViewModel
        model.craftList.observe(viewLifecycleOwner) { crafts ->
            // Update RecyclerView Adapter
            if (!crafts.isNullOrEmpty()){
                homeAdapter = HomeAdapter(crafts, this)
                recyclerView.adapter = homeAdapter
            } else {
                Log.d("HomeFragment", " list is empty or null")
            }
        }

        if (model.craftList.value.isNullOrEmpty()) {
            getCraft()
        }

        //val navController = requireActivity().findNavController(R.id.mobile_navigation)
        val cardScan = binding.card2
        cardScan.setOnClickListener{
            findNavController().navigate(R.id.action_to_fragmentScan)
        }

        val btnTutorial = binding.btnHow
        btnTutorial.setOnClickListener {
            findNavController().navigate(R.id.action_to_fragmentTutorial)
        }

        val btnBlog = binding.btnBlog
        btnBlog.setOnClickListener {
            findNavController().navigate(R.id.action_to_fragmentBlog)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCraft(){
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService()

        //load model
        val model = ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            HomeViewModel::class.java)

        apiService.getAllProject().enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                binding.progressBar.visibility = View.GONE

                val craft = response.body()?.data?.filterNotNull()
                model.setCraftList(craft)

            }
            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                // Handle failure scenario
                binding.progressBar.visibility = View.GONE
                Log.d("DetailFragmentError", "onFailure: $t")

            }

        })
    }

    override fun onItemClick(title: String?) {
        val bundle = Bundle().apply {
            putString("title", title) // Ensure your data class implements Parcelable
        }
        Log.d("ItemClick", "bundle: ${title}")

        findNavController().navigate(R.id.action_to_fragmentCraftDetail, bundle)
    }
}