package com.craftify.craftify_app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.craftify.craftify_app.MainActivity
import com.craftify.craftify_app.databinding.ActivitySplashBinding
import com.craftify.craftify_app.ui.login.LoginActivity
import com.craftify.craftify_app.ui.login.LoginViewModel
import com.craftify.craftify_app.utils.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val viewModel : LoginViewModel by viewModels { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.currentUser.observe(this){ user ->
                if (user != null) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, LoginActivity::class.java))
                }
                finish()
            }
        }, 2000)
    }
}