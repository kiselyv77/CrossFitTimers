package com.example.crossfit.fragments.mainSubfragments.fragmentHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crossfit.dataBase.WorkoutDataBase
import com.example.crossfit.models.Workout
import com.example.crossfit.models.toWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(val room: WorkoutDataBase):ViewModel() {

    private  val _state =  MutableStateFlow(HistoryFragmentState())
    val state = _state

    init{
        getList()
    }

    fun deleteWorkout(id:String){
        viewModelScope.launch {
            room.dao.deleteId(id)
        }
    }

    private fun getList(){
        viewModelScope.launch{
            _state.value = _state.value.copy(workoutList = room.dao.getWorkoutList().map { it.toWorkout() })
        }
    }
}