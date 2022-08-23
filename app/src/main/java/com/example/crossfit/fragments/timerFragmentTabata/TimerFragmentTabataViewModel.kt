package com.example.crossfit.fragments.timerFragmentTabata

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crossfit.fragments.timerFragmentEMOM.TimerFragmentEmomState
import com.example.crossfit.utils.formateTime2
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TimerFragmentTabataViewModel(
    private val countdownTime: Long,
    private val timeWork: Long,
    private val timeRest: Long,
    private val intervals: Long
) : ViewModel() {

    private  val _state =  MutableStateFlow<TimerFragmentTabataState>(TimerFragmentTabataState())
    val state = _state

    private lateinit var jobCountdownTimer: Job
    private lateinit var intervalTimer: Job

    private var timeCountWork:Long = TimeUnit.SECONDS.toSeconds(timeWork.toLong())
    private var timeCountRest:Long = TimeUnit.SECONDS.toSeconds(timeRest.toLong())

    init{
        _state.value = _state.value.copy(intervals = intervals.toInt())
        startTimerCountdown()
    }

    fun skip(){
        if(_state.value.isWorking){
            val skipTimeWork = TimeUnit.SECONDS.toSeconds(timeWork.toLong()) - timeCountWork
            _state.value = _state.value.copy(timeWork = _state.value.timeWork + skipTimeWork)
            //Log.d("skippingTime", _state.value.skippingTime.toString())
            _state.value.rounds.add(skipTimeWork.formateTime2())
            timeCountWork = TimeUnit.SECONDS.toSeconds(timeWork.toLong())
            intervalTimer.cancel()
            if(_state.value.isPlaying){
                startTimerRest()
            }
            else{
                _state.value = _state.value.copy(time = "00:00")
            }
        }
        else{
            val skipTimeRest = TimeUnit.SECONDS.toSeconds(timeRest.toLong()) - timeCountRest
            _state.value = _state.value.copy(timeWork = _state.value.timeWork + skipTimeRest)
            _state.value.rounds.add(skipTimeRest.formateTime2())
            _state.value = _state.value.copy(intervals = _state.value.intervals - 1)
            timeCountRest = TimeUnit.SECONDS.toSeconds(timeRest.toLong())
            intervalTimer.cancel()
            if(_state.value.intervals == 0){
                _state.value = _state.value.copy(timerEnd = true)
            }
            else{
                if(_state.value.isPlaying){
                    startTimerWork()
                }
                else{
                    _state.value = _state.value.copy(time = "00:00")
                }
            }
        }
    }

    fun pause(){
        intervalTimer.cancel()
        _state.value = _state.value.copy(isPlaying = false)
    }

    fun play(){
        if(_state.value.isWorking){
            startTimerWork()
        }
        else{
            startTimerRest()
        }
        _state.value = _state.value.copy(isPlaying = true)
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
            startTimerWork()
        }
    }

    private fun startTimerWork(){
        _state.value = _state.value.copy(isWorking = true)
        intervalTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(timeWork.toLong())
            val tickSeconds = 1
            for (second in timeCountWork downTo tickSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(timeCountWork),
                    timeCountWork - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCountWork))
                )
                Log.d("dd", "totalSeconds $totalSeconds")
                Log.d("dd", "timeCount $timeCountWork")
                val progress = (100 - timeCountWork.toDouble() / totalSeconds.toDouble() * 100).toInt()
                _state.value = _state.value.copy(time = time, progress = progress)
                timeCountWork -= 1
                delay(1000)
                Log.d("startTimerWork", time)
            }
            _state.value.rounds.add(_state.value.time)
            _state.value = _state.value.copy(time = "00:00")
            _state.value = _state.value.copy(progress = 100)
            timeCountWork = TimeUnit.SECONDS.toSeconds(timeWork.toLong())
            cancel()
            startTimerRest()
        }
    }

    private fun startTimerRest(){
        _state.value = _state.value.copy(isWorking = false)
        intervalTimer = viewModelScope.launch {
            val totalSeconds = TimeUnit.SECONDS.toSeconds(timeRest.toLong())
            val tickSeconds = 1
            for (second in timeCountRest downTo tickSeconds) {
                val time = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(timeCountRest),
                    timeCountRest - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(timeCountRest))
                )
                Log.d("dd", "totalSeconds $totalSeconds")
                Log.d("dd", "timeCount $timeCountRest")
                val progress = (100 - timeCountRest.toDouble() / totalSeconds.toDouble() * 100).toInt()
                _state.value = _state.value.copy(time = time, progress = progress)
                timeCountRest -= 1
                delay(1000)
                Log.d("startTimerRest", time)
            }
            Log.d("end", "end")
            _state.value.rounds.add(_state.value.time)
            _state.value = _state.value.copy(time = "00:00")
            _state.value = _state.value.copy(progress = 100)
            _state.value = _state.value.copy(intervals = _state.value.intervals - 1)
            if(_state.value.intervals == 0){
                _state.value = _state.value.copy(timerEnd = true)
                cancel()
            }
            timeCountRest = TimeUnit.SECONDS.toSeconds(timeRest.toLong())
            cancel()
            startTimerWork()
        }
    }

}