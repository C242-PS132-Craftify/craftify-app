package com.craftify.craftify_app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.craftify.craftify_app.MainActivity
import com.craftify.craftify_app.R
import com.craftify.craftify_app.databinding.ActivitySplashBinding
import com.craftify.craftify_app.ui.login.LoginActivity
import com.craftify.craftify_app.ui.login.LoginViewModel
import com.craftify.craftify_app.ui.onboarding.OnboardingViewModel
import com.craftify.craftify_app.ui.onboarding.ViewPagerFragment
import com.craftify.craftify_app.utils.ViewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySplashBinding
    private val viewModel : LoginViewModel by viewModels { ViewModelFactory(this) }
    private val onboardingViewModel : OnboardingViewModel by viewModels { ViewModelFactory(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_splash, SplashFragment())
                .commit()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onboardingViewModel.getThemeSettings().observe(this){ onboarding ->
                if(onboarding == false) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_splash, ViewPagerFragment())
                        .addToBackStack(null) // Optional: Add to back stack
                        .commit()
                }else{
                    //onboardingViewModel.saveThemeSetting(false)
                    viewModel.currentUser.observe(this){ user ->
                        if (user != null) {
                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        finish()
                    }
                }
            }
        }, 2000)
    }
}