package com.example.crossfit.fragments.mainSubfragments.fragmentTimers


data class TimersFragmentState(
    val stateCardForTime: StateCardForTime = StateCardForTime(),
    val stateCardAmrap: StateCardAmrap = StateCardAmrap(),
    val stateCardEmom: StateCardEmom = StateCardEmom(),
    val stateCardTabata: StateCardTabata = StateCardTabata()
)

data class StateCardForTime(
    val countDownTime: Long = 3,
    val autoEndTime:Long = 1800
)
data class StateCardAmrap(
    val countDownTime: Long = 3,
    val autoEndTime:Long = 1800
)
data class StateCardEmom(
    val countDownTime: Long = 3,
    val timeInterval:Long = 1800,
    val rounds:Long = 10

)
data class StateCardTabata(
    val countDownTime: Long = 3,
    val timeIntervalWork:Long = 1800,
    val timeIntervalRest:Long = 1800,
    val rounds:Long  = 10
)
