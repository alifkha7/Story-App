package com.hirocode.story.view.login

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.hirocode.story.R
import com.hirocode.story.databinding.ActivityLoginBinding
import com.hirocode.story.model.LoginResult
import com.hirocode.story.model.UserPreference
import com.hirocode.story.utils.SETTINGS
import com.hirocode.story.view.ViewModelFactory
import com.hirocode.story.view.main.MainActivity
import com.hirocode.story.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTINGS)

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.inLoginEmail.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.inLoginPassword.error = getString(R.string.input_password)
                }
                else -> {
                    loginViewModel.login(email, password)
                    loginViewModel.isLoading.observe(this) { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                    loginViewModel.response.observe(this) { response ->
                        Toast.makeText(this@LoginActivity, response?.message, Toast.LENGTH_LONG)
                            .show()
                    }
                    loginViewModel.loginResult.observe(this) { loginResult ->
                        val name = loginResult.name
                        val user = loginResult.userId
                        val token = loginResult.token
                        loginViewModel.saveUser(LoginResult(name, user, token, true))
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}