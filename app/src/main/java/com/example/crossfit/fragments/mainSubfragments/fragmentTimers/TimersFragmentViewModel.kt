package com.example.crossfit.fragments.mainSubfragments.fragmentTimers

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TimersFragmentViewModel: ViewModel() {

    private  val _state =  MutableStateFlow(TimersFragmentState())
    val state = _state

    fun updateCardForTimeCountDownTime(countDownTime: Long){
        _state.value = _state.value.copy(stateCardForTime = _state.value.stateCardForTime.copy(countDownTime = countDownTime))
    }
    fun updateCardForTimeAutoEndTime(autoEndTime:Long){
        _state.value = _state.value.copy(stateCardForTime = _state.value.stateCardForTime.copy(autoEndTime = autoEndTime))
    }

    fun updateCardAmrapCountDownTime(countDownTime:Long){
        _state.value = _state.value.copy(stateCardAmrap = _state.value.stateCardAmrap.copy(countDownTime = countDownTime))
    }
    fun updateCardAmrapAutoEndTime(autoEndTime:Long){
        _state.value = _state.value.copy(stateCardAmrap = _state.value.stateCardAmrap.copy(autoEndTime = autoEndTime))
    }

    fun updateCardEmomCountDownTime(countDownTime:Long){
        _state.value = _state.value.copy(stateCardEmom = _state.value.stateCardEmom.copy(countDownTime = countDownTime))
    }
    fun updateCardEmomAutoEndTime(autoEndTime:Long){
        _state.value = _state.value.copy(stateCardEmom = _state.value.stateCardEmom.copy(timeInterval = autoEndTime))
    }

    fun updateCardEmomCountRounds(rounds:Long){
        _state.value = _state.value.copy(stateCardEmom = _state.value.stateCardEmom.copy(rounds = rounds))
    }

    fun updateCardTabataCountDownTime(countDownTime:Long){
        _state.value = _state.value.copy(stateCardTabata = _state.value.stateCardTabata.copy(countDownTime = countDownTime))

    }

    fun updateCardTabataTimeIntervalWork(timeIntervalWork:Long){
        _state.value = _state.value.copy(stateCardTabata = _state.value.stateCardTabata.copy(timeIntervalWork = timeIntervalWork))
    }
    fun updateCardTabataTimeIntervalRest(timeIntervalRest:Long){
        _state.value = _state.value.copy(stateCardTabata = _state.value.stateCardTabata.copy(timeIntervalRest = timeIntervalRest))
    }

    fun updateCardTabataRounds(rounds:Long){
        _state.value = _state.value.copy(stateCardTabata = _state.value.stateCardTabata.copy(rounds = rounds))
    }


}