package com.example.crossfit.fragments.timerFragmentTabata

data class TimerFragmentTabataState(
    val time: String = "",
    val isPlaying:Boolean = false,
    val isCountdown:Boolean = true,
    val timeCountDownTimer:String = "",
    val timerEnd:Boolean = false,
    val intervals:Int = 10,
    val progress:Int = 0,
    val timeWork:Long = 0,
    val timeRest:Long = 0,
    val rounds: ArrayList<String> = arrayListOf(),
    val isWorking: Boolean = true
)
