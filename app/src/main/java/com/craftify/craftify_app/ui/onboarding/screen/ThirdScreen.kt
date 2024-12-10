package com.craftify.craftify_app.ui.onboarding.screen

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.craftify.craftify_app.R
import com.craftify.craftify_app.data.local.SettingPreferences
import com.craftify.craftify_app.data.local.dataStore
import com.craftify.craftify_app.databinding.FragmentFirstScreenBinding
import com.craftify.craftify_app.databinding.FragmentThirdScreenBinding
import com.craftify.craftify_app.ui.onboarding.OnboardingViewModel
import com.craftify.craftify_app.utils.ViewModelFactory


class ThirdScreen : Fragment() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentThirdScreenBinding.inflate(inflater, container, false)

        val viewPager =  activity?.findViewById<ViewPager2>(R.id.viewPager)

        val ViewModel = ViewModelProvider(this, ViewModelFactory(requireContext())).get(
            OnboardingViewModel::class.java
        )

        binding.tvNext.setOnClickListener {
            viewPager?.currentItem = 3
            val setting = true
            ViewModel.saveThemeSetting(setting)
        }

        return binding.root
    }

}