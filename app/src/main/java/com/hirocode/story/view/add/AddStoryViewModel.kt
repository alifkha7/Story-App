package com.hirocode.story.view.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.hirocode.story.api.ApiConfig
import com.hirocode.story.model.AuthResponse
import com.hirocode.story.model.LoginResult
import com.hirocode.story.model.UserPreference
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<AuthResponse?>()
    val response: LiveData<AuthResponse?> = _response

    fun getUser(): LiveData<LoginResult> {
        return pref.getUser().asLiveData()
    }

    fun uploadStory(token: String, file: MultipartBody.Part, description: RequestBody, lat: RequestBody, long: RequestBody) {
        _isLoading.value = true
        val service = ApiConfig.getApiService().uploadStory("Bearer $token", file, description, lat, long)
        service.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(
                call: Call<AuthResponse>,
                response: Response<AuthResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _isLoading.value = false
                        _response.value = responseBody
                        Log.i(TAG, "onResponse: ${responseBody.message}")
                    }
                } else {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "AddStoryViewModel"
    }
}