package com.example.crossfit.fragments.timerFragmentEMOM

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerFragmentEmomViewModel(private val countdownTime:Long, private val intervals:Long, private val intervalTime:Long): ViewModel() {
    private  val _state =  MutableStateFlow<TimerFragmentEmomState>(TimerFragmentEmomState())
    val state = _state

    private lateinit var intervalTimer: Job
    private lateinit var jobCountdownTimer: Job

    private var timeCount:Long = TimeUnit.SECONDS.toSeconds(intervalTime.toLong())

    init{
        _state.value = _state.value.copy(intervals = intervals.toInt())
        startTimerCountdown()
    }

    fun pause(){
        intervalTimer.cancel()
        _state.value = _state.value.copy(isPlaying = false)
    }

    fun play(){
        startTimer()
        _state.value = _state.value.copy(isPlaying = true)
    }

    fun skip(){
        val skipTime = TimeUnit.SECONDS.toSeconds(intervalTime.toLong()) - timeCount
        _state.value = _state.value.copy( skippingTime =  _state.value.skippingTime + skipTime)
        _state.value = _state.value.copy(intervals = _state.value.intervals - 1)
        if(_state.value.intervals == 0){
            _state.value = _state.value.copy(timerEnd = true)
        }
        restartTimer()
    }

    private fun restartTimer(){
        val skipTime = TimeUnit.SECONDS.toSeconds(intervalTime.toLong()) - timeCount
        val timeInterval = String.format("%02d:%02d",
            TimeUnit.SECONDS.toMinutes(timeCount - skipTime),
            (timeCount - skipTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCount - skipTime))
        )
        _state.value.rounds.add(timeInterval)
        timeCount = TimeUnit.SECONDS.toSeconds(intervalTime.toLong())
        intervalTimer.cancel()
        startTimer()
    }

    private fun startTimerCountdown(){
        jobCountdownTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(countdownTime.toLong())
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                _state.value = _state.value.copy(timeCountDownTimer = second.toString())
                delay(1000)
            }
            _state.value = _state.value.copy(isCountdown = false)
            _state.value = _state.value.copy(isPlaying = true)
            startTimer()
        }
    }

    private fun startTimer(){
        intervalTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(intervalTime.toLong())
            val tickSeconds = 1
            for (second in totalSeconds downTo tickSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(timeCount),
                    timeCount - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCount))
                )
                Log.d("dd", "totalSeconds $totalSeconds")
                Log.d("dd", "timeCount $timeCount")
                val progress = (100 - timeCount.toDouble() / totalSeconds.toDouble() * 100).toInt()
                _state.value = _state.value.copy(time = time, progress = progress)
                timeCount -= 1
                delay(1000)
            }
            Log.d("end", "end")
            _state.value = _state.value.copy(time = "00:00")
            _state.value = _state.value.copy(progress = 100)
            _state.value = _state.value.copy(intervals = _state.value.intervals - 1)
            if(_state.value.intervals == 0){
                _state.value = _state.value.copy(timerEnd = true)
            }
            restartTimer()
        }
    }

}


