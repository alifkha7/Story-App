package com.hirocode.story.view

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hirocode.story.databinding.ItemStoryBinding
import com.hirocode.story.model.ListStoryItem
import com.hirocode.story.view.detail.DetailStoryActivity
import androidx.core.util.Pair

class StoryAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listStory.size

    class ViewHolder(private var binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: ListStoryItem) {
            binding.apply {
                Glide.with(itemView)
                    .load(storyItem.photoUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(ivItemPhoto)
                tvItemName.text = storyItem.name
                tvItemDescription.text = storyItem.description

//                itemView.setOnClickListener {
//                    val intent = Intent(itemView.context, DetailStoryActivity::class.java)
//                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, storyItem)
//
//                    val optionsCompat: ActivityOptionsCompat =
//                        ActivityOptionsCompat.makeSceneTransitionAnimation(
//                            itemView.context as Activity,
//                            Pair(ivItemPhoto, "photo"),
//                            Pair(tvItemName, "name"),
//                            Pair(tvItemDescription, "description"),
//                        )
//                    itemView.context.startActivity(intent, optionsCompat.toBundle())
//                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListStoryItem)
    }
}