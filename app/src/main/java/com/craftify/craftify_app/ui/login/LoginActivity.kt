package com.craftify.craftify_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.craftify.craftify_app.MainActivity
import com.craftify.craftify_app.databinding.FragmentLoginBinding
import com.craftify.craftify_app.utils.ViewModelFactory
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.ui.register.RegisterActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels {ViewModelFactory(applicationContext)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is  Result.Success-> {
                   navigateToHome()
                }
                is Result.Error -> {
                    Toast.makeText(this,result.error, Toast.LENGTH_SHORT ).show()
                }
                else -> {}
            }
        }
        loginViewModel.currentUser.observe(this){ user ->
            if (user != null) {
                navigateToHome()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "error",Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}