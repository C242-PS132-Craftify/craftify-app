package com.craftify.craftify_app.ui.savedCraft

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.databinding.ItemSavedCraftBinding
import com.craftify.craftify_app.ui.craft.DetailCraftFragment
import com.craftify.craftify_app.ui.result.OnCraftSaveListener
import com.craftify.craftify_app.ui.result.OnItemClickListener
import com.craftify.craftify_app.ui.result.RecommendationAdapter

class SavedCraftAdapter : ListAdapter<RecommendationsItem, SavedCraftAdapter.MyViewHolder>(DIFF_CALLBACK){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemSavedCraftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener{
            val bundle = Bundle().apply {
                putString("title", event.projectName)
            }
            holder.itemView.findNavController().navigate(R.id.action_savedcraft_to_detail_craft, bundle)
        }
    }
    class MyViewHolder(val binding: ItemSavedCraftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(craft : RecommendationsItem){
            binding.titleCard.text = craft.projectName
            Glide.with(binding.root.context)
                .load(craft.projectImg)
                .placeholder(R.drawable.ic_logo)
                .into(binding.imgCard)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationsItem>() {
            override fun areItemsTheSame(oldItem:RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem.projectName == newItem.projectName
            }
            override fun areContentsTheSame(oldItem: RecommendationsItem, newItem: RecommendationsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
