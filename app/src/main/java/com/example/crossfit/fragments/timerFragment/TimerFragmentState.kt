package com.example.crossfit.fragments.timerFragment


data class TimerFragmentState(
    val time: String = "",
    val timeCurrentRound: String = "",
    val isPlaying:Boolean = false,
    val isCountdown:Boolean = true,
    val timeCountDownTimer:String = "",
    val timerEnd:Boolean = false,
    val rounds: ArrayList<String> = arrayListOf()
)
