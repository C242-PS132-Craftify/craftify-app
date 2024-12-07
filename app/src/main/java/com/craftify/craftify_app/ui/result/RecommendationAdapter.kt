package com.craftify.craftify_app.ui.result

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.response.RecommendationsItem

class RecommendationAdapter(private val recommen: List<RecommendationsItem>, private val listener: OnItemClickListener) :

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

        // Set click listener
        holder.itemView.setOnClickListener {
            listener.onItemClick(recommendation.projectName)
            Log.d("Data Ser", "data : ${recommendation.projectName}")
        }
    }

    override fun getItemCount(): Int = recommen.size

}