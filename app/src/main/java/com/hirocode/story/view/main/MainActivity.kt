package com.hirocode.story.view.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hirocode.story.R
import com.hirocode.story.databinding.ActivityMainBinding
import com.hirocode.story.databinding.ItemStoryBinding
import com.hirocode.story.model.ListStoryItem
import com.hirocode.story.model.UserPreference
import com.hirocode.story.utils.SETTINGS
import com.hirocode.story.view.StoryAdapter
import com.hirocode.story.view.ViewModelFactory
import com.hirocode.story.view.add.AddStoryActivity
import com.hirocode.story.view.detail.DetailStoryActivity
import com.hirocode.story.view.login.LoginActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    lateinit var itemStoryBinding: ItemStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        itemStoryBinding = ItemStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[MainViewModel::class.java]

        mainViewModel.getUser().observe(this) { user ->
            if (user.isLogin) {
                mainViewModel.getAllStories(user.token)
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mainViewModel.listStory.observe(this) { storyItem ->
            binding.apply {
                rvStories.setHasFixedSize(true)
                rvStories.layoutManager = LinearLayoutManager(this@MainActivity)
                val listStoryAdapter = StoryAdapter(storyItem)
                rvStories.adapter = listStoryAdapter

                listStoryAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback{
                    override fun onItemClicked(data: ListStoryItem) {
                        val intent = Intent(this@MainActivity, DetailStoryActivity::class.java)
                        intent.putExtra(DetailStoryActivity.EXTRA_STORY, data)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@MainActivity,
                                Pair(itemStoryBinding.ivItemPhoto, "photo"),
                                Pair(itemStoryBinding.tvItemName, "name"),
                                Pair(itemStoryBinding.tvItemDescription, "description"),
                            )
                        startActivity(intent, optionsCompat.toBundle())
                    }

                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add -> {
                startActivity(Intent(this, AddStoryActivity::class.java))
                true
            }
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            R.id.logout -> {
                mainViewModel.logout()
                true
            }
            else -> true
        }
    }
}