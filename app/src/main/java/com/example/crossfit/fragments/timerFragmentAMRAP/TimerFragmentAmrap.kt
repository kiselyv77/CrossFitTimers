package com.example.crossfit.fragments.timerFragmentAMRAP

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.adapters.RoundAdapter
import com.example.crossfit.databinding.FragmentTimerBinding
import com.example.crossfit.fragments.MyViewModelFactory
import com.example.crossfit.models.WorkoutType.TYPE_AMRAP
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch


class TimerFragmentAmrap : Fragment() {
    lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        val binding = FragmentTimerBinding.inflate(inflater, container, false)
        val countdownTime = arguments?.getLong("countdownTime")?: 5
        val timeEnd = arguments?.getLong("timeStart")?: 2
        val viewModel = ViewModelProvider(this, MyViewModelFactory(countdownTime, timeEnd)).get(
            TimerFragmentAmrapViewModel::class.java)
        val adapter = RoundAdapter()
        binding.rcView.adapter = adapter
        binding.rcView.layoutManager = LinearLayoutManager(requireContext())
        binding.back.setOnClickListener { NAV_CONTROLLER.popBackStack() }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 200
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.apply {
                    timerText.text = state.time
                    countRound.text = state.timeCurrentRound
                    addRound.setOnClickListener {
                        viewModel.addRound()
                        if(state.rounds.isNotEmpty()){
                            adapter.setList(state.rounds)
                        }
                    }
                    pausePlay.setOnClickListener {
                        if (state.isPlaying){
                            viewModel.pause()
                            pausePlay.setImageResource(R.drawable.play)
                        }
                        else{
                            viewModel.play()
                            pausePlay.setImageResource(R.drawable.pause)
                        }
                    }
                    stop.setOnClickListener {
                        val bundle = Bundle()
                        viewModel.addRound()
                        bundle.putStringArrayList("rounds", state.rounds)
                        bundle.putString("time", state.time)
                        bundle.putString("type", TYPE_AMRAP)
                        NAV_CONTROLLER.navigate(R.id.saveFragment, bundle)
                    }
                    if(state.isCountdown){
                        countDown.visibility = View.VISIBLE
                        countDown.text = state.timeCountDownTimer

                        stop.visibility = View.GONE
                        pausePlay.visibility = View.GONE
                        addRound.visibility = View.GONE
                    }
                    else{
                        countDown.visibility = View.INVISIBLE

                        stop.visibility = View.VISIBLE
                        pausePlay.visibility = View.VISIBLE
                        addRound.visibility = View.VISIBLE
                    }
                    if(state.timerEnd){
                        val bundle = Bundle()
                        bundle.putStringArrayList("rounds", state.rounds)
                        bundle.putString("time", state.time)
                        bundle.putString("type", TYPE_AMRAP)
                        NAV_CONTROLLER.navigate(R.id.saveFragment, bundle)
                    }
                }
            }
        }
        return binding.root

    }

}