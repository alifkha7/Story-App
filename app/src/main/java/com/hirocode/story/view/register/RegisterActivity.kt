package com.hirocode.story.view.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hirocode.story.R
import com.hirocode.story.databinding.ActivityRegisterBinding
import com.hirocode.story.view.login.LoginActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    private fun setupAction() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            when {
                name.isEmpty() -> {
                    binding.inRegisterName.error = getString(R.string.input_name)
                }
                email.isEmpty() -> {
                    binding.inRegisterEmail.error = getString(R.string.input_email)
                }
                password.isEmpty() -> {
                    binding.inRegisterPassword.error = getString(R.string.input_password)
                }
                else -> {
                    registerViewModel.register(name, email, password)
                    registerViewModel.isLoading.observe(this) { isLoading ->
                        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                    }
                    registerViewModel.response.observe(this) { response ->
                        Toast.makeText(this@RegisterActivity, response?.message, Toast.LENGTH_LONG)
                            .show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        binding.login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}