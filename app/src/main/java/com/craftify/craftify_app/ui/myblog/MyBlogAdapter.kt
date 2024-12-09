package com.craftify.craftify_app.ui.myblog

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.model.Blog
import com.craftify.craftify_app.databinding.ItemMyBlogBinding
import com.craftify.craftify_app.ui.blog.BlogDetailsActivity

class MyBlogAdapter (
    private var context : Context,
    private val onEdit : (Blog) -> Unit,
    private val onDelete : (Blog) -> Unit
) : ListAdapter<Blog, MyBlogAdapter.MyBlogViewHolder>(DIFF_CALLBACK) {
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

    inner class MyBlogViewHolder(private val binding: ItemMyBlogBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Blog) {
            binding.tvTitle.text = item.title
            binding.tvAuthor.text = item.author
            Glide.with(binding.ivHeaderImage.context)
                .load(item.headerImage)
                .placeholder(R.drawable.ic_logo)
                .into(binding.ivHeaderImage)

            itemView.setOnClickListener {
                item.id.let { id ->
                    val intent = Intent(context, BlogDetailsActivity::class.java).apply {
                        putExtra("BLOG_ID", id)
                    }
                    context.startActivity(intent)
                }
            }

            binding.btnMenu.setOnClickListener {
                showPopupMenu(it, item)
            }
        }

        private fun showPopupMenu(view: View, item: Blog) {
            val popupMenu = PopupMenu(view.context, view)
            val inflater : MenuInflater = popupMenu.menuInflater
            inflater.inflate(R.menu.my_blog_options_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_edit -> {
                        onEdit(item)
                        true
                    }
                    R.id.action_delete -> {
                        onDelete(item)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBlogViewHolder {
        val binding = ItemMyBlogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBlogViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBlogViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}