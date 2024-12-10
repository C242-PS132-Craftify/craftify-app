package com.craftify.craftify_app.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import com.craftify.craftify_app.R
import com.craftify.craftify_app.databinding.FragmentThirdScreenBinding
import com.craftify.craftify_app.databinding.FragmentViewPagerBinding
import com.craftify.craftify_app.ui.onboarding.screen.FirstScreen
import com.craftify.craftify_app.ui.onboarding.screen.SecondScreen
import com.craftify.craftify_app.ui.onboarding.screen.ThirdScreen

class ViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf<Fragment>(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = adapter

        return binding.root
    }

}