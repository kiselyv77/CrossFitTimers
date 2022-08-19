package com.example.crossfit.fragments.timerFragmentEMOM

data class TimerFragmentEmomState(
    val time: String = "",
    val isPlaying:Boolean = false,
    val isCountdown:Boolean = true,
    val timeCountDownTimer:String = "",
    val timerEnd:Boolean = false,
    val intervals:Int = 10,
    val progress:Int = 0,
    val skippingTime:Long = 0,
    val rounds: ArrayList<String> = arrayListOf()
)
