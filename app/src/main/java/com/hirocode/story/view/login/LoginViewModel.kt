package com.hirocode.story.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hirocode.story.api.ApiConfig
import com.hirocode.story.model.AuthResponse
import com.hirocode.story.model.LoginResult
import com.hirocode.story.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<AuthResponse>()
    val response: LiveData<AuthResponse> = _response

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(email: String, password: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                    _loginResult.value = response.body()?.loginResult
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })

        viewModelScope.launch {
            pref.login()
        }
    }

    fun saveUser(user: LoginResult) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    companion object{
        private const val TAG = "LoginViewModel"
    }
}