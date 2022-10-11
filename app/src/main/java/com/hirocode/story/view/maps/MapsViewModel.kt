package com.hirocode.story.view.maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hirocode.story.api.ApiConfig
import com.hirocode.story.model.ListStoryItem
import com.hirocode.story.model.LoginResult
import com.hirocode.story.model.StoryResponse
import com.hirocode.story.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(private val pref: UserPreference) : ViewModel() {
    private val _listStory = MutableLiveData<ArrayList<ListStoryItem>>()
    val listStory: LiveData<ArrayList<ListStoryItem>> = _listStory

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun getAllStories(token: String) {
        val client = ApiConfig.getApiService().storiesLoc("Bearer $token", 1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _listStory.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "MapsViewModel"
    }
}