package com.example.crossfit.fragments.timerFragmentTabata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.crossfit.R
import com.example.crossfit.databinding.FragmentTimerBinding
import com.example.crossfit.databinding.FragmentTimerTabataBinding


class TimerFragmentTabata : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTimerTabataBinding.inflate(inflater, container, false)
        return binding.root
    }

}