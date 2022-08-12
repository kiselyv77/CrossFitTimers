package com.example.crossfit.fragments.timerFragmentEMOM

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.databinding.FragmentTimerBinding
import com.example.crossfit.databinding.FragmentTimerEmomBinding
import com.example.crossfit.fragments.MyViewModelFactory
import com.example.crossfit.fragments.timerFragmentAMRAP.TimerFragmentAmrapViewModel
import com.example.crossfit.models.WorkoutType.TYPE_EMOM
import kotlinx.coroutines.launch


class TimerFragmentEmom : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTimerEmomBinding.inflate(inflater, container, false)

        val countdownTime = arguments?.getLong("countdownTime")?: 5
        val time = arguments?.getLong("timeInterval")?: 1
        val intervals = arguments?.getLong("intervals")?: 5
        val viewModel = ViewModelProvider(this, MyViewModelFactory(countdownTime, intervals = intervals, timeEnd = time)).get(
            TimerFragmentEmomViewModel::class.java)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.apply {
                    timerText.text  = state.time
                    this.intervals.text = state.intervals.toString()
                    progressBar.progress = state.progress

                    btnSkip.setOnClickListener {
                        viewModel.skip()
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

                    if(state.isCountdown){
                        countDown.visibility = View.VISIBLE
                        countDown.text = state.timeCountDownTimer
                    }
                    else{
                        countDown.visibility = View.INVISIBLE
                    }

                    back.setOnClickListener{
                        NAV_CONTROLLER.popBackStack()
                    }

                    if(state.timerEnd){
                        val bundle = Bundle()
                        bundle.putStringArrayList("rounds", state.rounds)
                        bundle.putString("time", (time*intervals-state.skippingTime).toString())
                        bundle.putString("type", TYPE_EMOM)
                        NAV_CONTROLLER.navigate(R.id.saveFragment, bundle)
                    }
                }
            }
        }
        return binding.root
    }

}