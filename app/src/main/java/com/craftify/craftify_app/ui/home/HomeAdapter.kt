package com.craftify.craftify_app.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.response.DataItem
import com.craftify.craftify_app.data.server.response.DetailResponse
import com.craftify.craftify_app.databinding.FragmentHomeBinding
import com.craftify.craftify_app.ui.result.OnItemClickListener

class HomeAdapter(private val items: List<DataItem>, private val listener: OnItemClickListener) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.carousel_image_view)
        val itemText: TextView = view.findViewById(R.id.carousel_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carousel, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Log.d("Hommee page", "onBindViewHolder: ${item.projectImg} and ${item.projectName}")
        Glide.with(holder.itemView.context)
            .load(item.projectImg)
            .into(holder.itemImage)
        holder.itemText.text = item.projectName

        holder.itemView.setOnClickListener {
            listener.onItemClick(item.projectName)
            Log.d("Data Ser", "data : ${item.projectName}")
        }
    }

    override fun getItemCount(): Int = items.size


}
