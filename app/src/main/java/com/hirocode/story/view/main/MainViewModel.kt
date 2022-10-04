package com.hirocode.story.view.main

import android.util.Log
import androidx.lifecycle.*
import com.hirocode.story.api.ApiConfig
import com.hirocode.story.model.ListStoryItem
import com.hirocode.story.model.LoginResult
import com.hirocode.story.model.StoryResponse
import com.hirocode.story.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listStory = MutableLiveData<ArrayList<ListStoryItem>>()
    val listStory: LiveData<ArrayList<ListStoryItem>> = _listStory

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun getAllStories(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().stories("Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    _isLoading.value = false
                    _listStory.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}