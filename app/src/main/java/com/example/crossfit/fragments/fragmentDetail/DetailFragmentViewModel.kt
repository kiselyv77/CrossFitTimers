package com.example.crossfit.fragments.fragmentDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crossfit.dataBase.WorkoutDataBase
import com.example.crossfit.fragments.mainSubfragments.fragmentHistory.HistoryFragmentState
import com.example.crossfit.models.toWorkout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailFragmentViewModel @Inject constructor(val room: WorkoutDataBase): ViewModel() {

    private  val _state =  MutableStateFlow(DetailFragmentState())
    val state = _state


    fun getWorkout(id:String){
        viewModelScope.launch{
            _state.value = _state.value.copy(room.dao.getWorkoutById(id).toWorkout())
        }
    }
}