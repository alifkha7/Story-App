package com.hirocode.story.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hirocode.story.R
import com.hirocode.story.databinding.ActivityDetailStoryBinding
import com.hirocode.story.model.ListStoryItem

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupView()
    }

    private fun setupActionBar() {
        supportActionBar!!.title = getString(R.string.posts)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupView() {
        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY) as ListStoryItem
        binding.apply {
            tvItemName.text = story.name
            Glide.with(this@DetailStoryActivity)
                .load(story.photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(ivItemPhoto)
            tvDetailDescription.text = story.description
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}