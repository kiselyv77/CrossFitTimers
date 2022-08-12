package com.example.crossfit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crossfit.databinding.FragmentMainBinding
import com.example.crossfit.utils.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val viewPager = binding.viewPager
        val tabs = binding.tabs
        viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position){
                0->{tab.text = "Timer"}
                1->{tab.text = "History"}
            }
        }.attach()

        viewPager.currentItem =



        return binding.root
    }

}