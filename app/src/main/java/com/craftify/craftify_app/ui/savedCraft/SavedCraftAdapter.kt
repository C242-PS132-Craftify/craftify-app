package com.craftify.craftify_app.ui.savedCraft

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.local.entity.RecommendationEntity
import com.craftify.craftify_app.data.server.response.RecommendationsItem
import com.craftify.craftify_app.ui.result.OnCraftSaveListener
import com.craftify.craftify_app.ui.result.OnItemClickListener
import com.craftify.craftify_app.ui.result.RecommendationAdapter

class SavedCraftAdapter(
    private val craft: List<RecommendationEntity?>,
    private val listener: OnItemClickListener,
) :
    RecyclerView.Adapter<SavedCraftAdapter.SavedCraftViewHolder>() {

    class SavedCraftViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.img_card_craft)
        val title: TextView = view.findViewById(R.id.title_card_craft)
        val bookmark: ImageView = view.findViewById(R.id.fav_icon_craft)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCraftViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_saved_craft, parent, false)
        return SavedCraftViewHolder(view)
    }

    override fun getItemCount(): Int = craft.size

    override fun onBindViewHolder(holder: SavedCraftViewHolder, position: Int) {
        val savedcraft = craft[position]
        holder.title.text = savedcraft?.title
        Glide.with(holder.itemView.context)
            .load(savedcraft?.image)
            .into(holder.image)

        // Set click listener
        holder.itemView.setOnClickListener {
            listener.onItemClick(savedcraft?.title)
        }
    }
}