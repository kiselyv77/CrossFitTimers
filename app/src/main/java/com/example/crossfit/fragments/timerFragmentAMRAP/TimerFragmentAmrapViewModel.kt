package com.example.crossfit.fragments.timerFragmentAMRAP

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crossfit.fragments.timerFragment.TimerFragmentState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerFragmentAmrapViewModel(private val countdownTime:Long, private val timeStart:Long) : ViewModel() {
    private  val _state =  MutableStateFlow<TimerFragmentAmrapState>(TimerFragmentAmrapState())
    val state = _state

    private lateinit var jobMainTimer: Job
    private lateinit var jobRoundTimer: Job
    private lateinit var jobCountdownTimer: Job
    private var timeCountMain:Long = 0
    private var timeCountRound:Long = 0


    init{
        startTimerCountdown()
    }

    fun addRound(){
        _state.value.rounds.add(_state.value.timeCurrentRound)
        jobRoundTimer.cancel()
        _state.value = _state.value.copy(timeCurrentRound = "00:00")
        timeCountRound = 0
        if(jobMainTimer.isActive){
            startTimerRound()
        }
    }

    fun pause(){
        jobMainTimer.cancel()
        jobRoundTimer.cancel()
        _state.value = _state.value.copy(isPlaying = false)
    }

    fun play(){
        startTimer()
        startTimerRound()
        _state.value = _state.value.copy(isPlaying = true)
    }

    fun stop(){
        pause()
        timeCountMain = 0
        timeCountRound = 0
        _state.value = _state.value.copy(time = "00:00", timeCurrentRound = "00:00", rounds = arrayListOf())
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
            timeCountMain = timeStart
            startTimer()
            startTimerRound()
        }
    }

    private fun startTimer (){
        jobMainTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(timeStart.toLong())
            val tickSeconds = 1
            for (second in timeCountMain downTo tickSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(timeCountMain),
                    timeCountMain - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCountMain))
                )
                _state.value = _state.value.copy(time)
                timeCountMain -= 1
                delay(1000)
            }
            _state.value = _state.value.copy(timerEnd = true)
        }
    }

    private fun startTimerRound(){
        jobRoundTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(timeStart.toLong())
            val tickSeconds = 1
            for (second in tickSeconds..totalSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(timeCountRound),
                    timeCountRound - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCountRound))
                )
                _state.value = _state.value.copy(timeCurrentRound = time)
                timeCountRound += 1
                delay(1000)
            }
        }
    }

}