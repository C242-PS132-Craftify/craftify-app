package com.craftify.craftify_app.ui.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.craftify.craftify_app.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.registerUser(email, username, password)
        }

        viewModel.registrationState.observe(this, Observer { result ->
            result?.let {
                if (it.success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    //TODO navigate to another screen
                } else {
                    Toast.makeText(this, it.error ?: "An unknown error occurred", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}
