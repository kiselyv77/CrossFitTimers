package com.example.crossfit.fragments.timerFragmentTabata

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.crossfit.NAV_CONTROLLER
import com.example.crossfit.R
import com.example.crossfit.databinding.FragmentTimerBinding
import com.example.crossfit.databinding.FragmentTimerTabataBinding
import com.example.crossfit.fragments.MyViewModelFactory
import com.example.crossfit.fragments.timerFragmentEMOM.TimerFragmentEmomViewModel
import com.example.crossfit.models.WorkoutType
import com.example.crossfit.utils.formateTime
import com.example.crossfit.utils.formateTime2
import kotlinx.coroutines.launch


class TimerFragmentTabata : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentTimerTabataBinding.inflate(inflater, container, false)

        val countdownTime = arguments?.getLong("countdownTime")?: 5
        val timeWork = arguments?.getLong("timeWork")?: 1
        val timeRest = arguments?.getLong("timeRest")?: 5
        val intervals = arguments?.getLong("intervals")?: 5

        val viewModel = ViewModelProvider(this, MyViewModelFactory(
            countdownTime = countdownTime,
            timeWork = timeWork,
            timeRest = timeRest,
            intervals = intervals)).get(TimerFragmentTabataViewModel::class.java)


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.apply {
                    timerText.text  = state.time
                    this.intervals.text = state.intervals.toString()
                    progressBar.progress = state.progress

                    if(state.isCountdown){
                        countDown.visibility = View.VISIBLE
                        countDown.text = state.timeCountDownTimer
                    }
                    else{
                        countDown.visibility = View.INVISIBLE
                    }

                    if(state.isWorking){
                        background.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorWork))
                        status.text = "Тренеровка"
                    }
                    else{
                        background.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorRest))
                        status.text = "Отдых"
                    }
                    if(state.timerEnd){
                        val bundle = Bundle()
                        bundle.putStringArrayList("rounds", state.rounds)
                        bundle.putString("time", ((timeWork+timeRest)*intervals-state.skippingTime).formateTime())
                        bundle.putString("type", WorkoutType.TYPE_TABATA)
                        NAV_CONTROLLER.navigate(R.id.saveFragment, bundle)
                    }

                    btnSkip.setOnClickListener {
                        viewModel.skip()
                    }
                    back.setOnClickListener {
                        NAV_CONTROLLER.popBackStack()
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
                }
            }
        }
        return binding.root
    }

}