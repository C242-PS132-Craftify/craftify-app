package com.craftify.craftify_app.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.DetectionsItem
import com.craftify.craftify_app.data.server.api.RecommendationsItem

class RecommendationAdapter(private val recommen: List<RecommendationsItem>) :

    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    class RecommendationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.img_card)
        val title: TextView = view.findViewById(R.id.title_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommendation, parent, false)
        return RecommendationViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val recommendation = recommen[position]
        holder.title.text = "${recommendation.projectName}"
        Glide.with(holder.itemView.context)
            .load(recommendation.projectImg)
            .into(holder.image)
    }

    override fun getItemCount(): Int = recommen.size

}