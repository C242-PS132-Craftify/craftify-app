package com.craftify.craftify_app.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.craftify.craftify_app.MainActivity
import com.craftify.craftify_app.data.Result
import com.craftify.craftify_app.databinding.ActivityRegisterBinding
import com.craftify.craftify_app.ui.login.LoginActivity
import com.craftify.craftify_app.utils.AuthValidation
import com.craftify.craftify_app.utils.CustomLoadingDialog
import com.craftify.craftify_app.utils.ViewModelFactory
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels{ ViewModelFactory(applicationContext) }

    private lateinit var loadingDialog: CustomLoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingDialog = CustomLoadingDialog(this)

        setupListeners()
        observerViewModel()
    }

    private fun observerViewModel() {
        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    loadingDialog.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is Result.Error -> {
                    loadingDialog.dismiss()
                    Toast.makeText(this, "Register Error", Toast.LENGTH_SHORT).show()
                }

                Result.Loading -> {
                    loadingDialog.show()
                }
            }
        }
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            val errorMessage = AuthValidation.validateAll(username, email, password)
            if (errorMessage.isEmpty()) {
                viewModel.register(username, email, password)
            } else {
                showValidationErrors(errorMessage)
            }
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showValidationErrors(errors: List<String>) {
        errors.forEach {
            when {
                it.contains("username") -> binding.teUsername.error = it
                it.contains("email") -> binding.teEmail.error = it
                it.contains("password") -> binding.tePassword.error = it
            }
        }
    }
}
