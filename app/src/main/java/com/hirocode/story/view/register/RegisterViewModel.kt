package com.hirocode.story.view.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hirocode.story.api.ApiConfig
import com.hirocode.story.model.AuthResponse
import com.hirocode.story.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel() : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<AuthResponse>()
    val response: LiveData<AuthResponse> = _response

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                } else {
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
        private const val TAG = "RegisterViewModel"
    }
}