package com.craftify.craftify_app.ui.blog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.model.Blog
import com.craftify.craftify_app.databinding.ItemBlogBinding

class BlogAdapter (private val onBlogClick : (String) -> Unit) : ListAdapter<Blog, BlogAdapter.BlogViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Blog>() {
            override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class BlogViewHolder(private val binding: ItemBlogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Blog) {
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.author
            binding.tvContentPreview.text = item.content
            Glide.with(binding.ivHeaderImage.context)
                .load(item.headerImage)
                .placeholder(R.drawable.ic_placeholder_image)
                .into(binding.ivHeaderImage)

            itemView.setOnClickListener {
                item.id.let { id ->
                    onBlogClick(id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val binding = ItemBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
