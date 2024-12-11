package com.craftify.craftify_app.ui.result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.server.response.DetectionsItem
import java.util.Locale

class DetectionsAdapter(private val detections: List<DetectionsItem>) :
    RecyclerView.Adapter<DetectionsAdapter.DetectionViewHolder>() {

    class DetectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val classTextView: TextView = view.findViewById(R.id.textViewDetectionClass)
        val scoreTextView: TextView = view.findViewById(R.id.textViewDetectionScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detection, parent, false)
        return DetectionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetectionViewHolder, position: Int) {
        val detection = detections[position]

        val className = detection.jsonMemberClass?.replace("_", " ")?.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        val percentage = (detection.score?.times(100))?.toInt()

        holder.classTextView.text = "Class: $className"
        holder.scoreTextView.text = "Score: $percentage%"
    }

    override fun getItemCount(): Int = detections.size
}
