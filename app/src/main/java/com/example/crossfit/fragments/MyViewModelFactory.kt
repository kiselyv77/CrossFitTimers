package com.example.crossfit.fragments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crossfit.fragments.timerFragment.TimerFragmentViewModel
import com.example.crossfit.fragments.timerFragmentAMRAP.TimerFragmentAmrapViewModel
import com.example.crossfit.fragments.timerFragmentEMOM.TimerFragmentEmomViewModel
import com.example.crossfit.fragments.timerFragmentTabata.TimerFragmentTabataViewModel


// Override ViewModelProvider.NewInstanceFactory to create the ViewModel (VM).
class MyViewModelFactory(
    private val countdownTime:Long = 5,
    private val timeEnd:Long = 2,
    private val intervals:Long = 10,
    private val timeWork:Long = 1,
    private val timeRest:Long = 1
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerFragmentViewModel::class.java)) {
            return TimerFragmentViewModel(countdownTime, timeEnd) as T
        }
        if(modelClass.isAssignableFrom(TimerFragmentAmrapViewModel::class.java)){
            return TimerFragmentAmrapViewModel(countdownTime, timeEnd) as T
        }
        if(modelClass.isAssignableFrom(TimerFragmentEmomViewModel::class.java)){
            return TimerFragmentEmomViewModel(countdownTime, intervals, timeEnd) as T
        }
        if(modelClass.isAssignableFrom(TimerFragmentTabataViewModel::class.java)){
            return TimerFragmentTabataViewModel(countdownTime, timeWork, timeRest, intervals) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")

    }
}


