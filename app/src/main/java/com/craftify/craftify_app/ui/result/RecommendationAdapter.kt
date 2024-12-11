package com.craftify.craftify_app.ui.result

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.databinding.ItemRecommendationBinding
import com.craftify.craftify_app.ui.craft.DetailCraftFragment

class RecommendationAdapter(
) : ListAdapter<RecommendationsItem, RecommendationAdapter.RecommendationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener{
            val bundle = Bundle().apply {
                putString("title", event.projectName)
            }
            holder.itemView.findNavController().navigate(R.id.action_to_fragmentCraftDetail, bundle)
        }
    }

    inner class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: RecommendationsItem) {
            binding.titleCard.text = recommendation.projectName
            Glide.with(binding.root.context)
                .load(recommendation.projectImg)
                .into(binding.imgCard)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem.projectName == newItem.projectName
            }

            override fun areContentsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
