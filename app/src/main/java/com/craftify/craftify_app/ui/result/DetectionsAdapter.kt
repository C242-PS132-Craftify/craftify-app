package com.craftify.craftify_app.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.api.DetectionsItem

class DetectionsAdapter(private val detections: List<DetectionsItem>) :
    RecyclerView.Adapter<DetectionsAdapter.DetectionViewHolder>() {

    class DetectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textViewDetection)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detection, parent, false)
        return DetectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetectionViewHolder, position: Int) {
        val detection = detections[position]
        holder.textView.text = "${position + 1}. ${detection.jsonMemberClass}" // Assuming `box` contains the name.
    }

    override fun getItemCount(): Int = detections.size
}
