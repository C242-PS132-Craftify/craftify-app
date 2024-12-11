package com.craftify.craftify_app.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.craftify.craftify_app.MainActivity
import com.craftify.craftify_app.databinding.FragmentLoginBinding
import com.craftify.craftify_app.utils.ViewModelFactory
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.ui.register.RegisterActivity
import com.craftify.craftify_app.utils.CustomLoadingDialog


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels {ViewModelFactory(applicationContext)}
    private lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = CustomLoadingDialog(this)
        loginViewModel.loginResult.observe(this) { result ->
            when (result) {
                is  Result.Success-> {
                    loadingDialog.dismiss()
                   navigateToHome()
                }
                is Result.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this,"Email or Password Incorrect", Toast.LENGTH_SHORT ).show()
                }
                else -> {
                    loadingDialog.show()
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(email, password)
            } else {
                Toast.makeText(this, "Email and Password is Required",Toast.LENGTH_SHORT).show()
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