package com.example.crossfit.fragments

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.getColor
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.example.crossfit.R
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
                0->{
                    tab.text = "Таймеры"
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_timer)
                }
                1->{
                    tab.text = "История"
                    tab.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_history)
                }

            }
        }.attach()


        return binding.root
    }

}