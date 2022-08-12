package com.example.crossfit.fragments.saveFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crossfit.dataBase.WorkoutDataBase
import com.example.crossfit.models.Workout
import com.example.crossfit.models.toWorkoutEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveFragmentViewModel @Inject constructor(val room: WorkoutDataBase): ViewModel() {

    fun saveWorkout(title:String, type:String, description:String, time:String, number:String, dateTime:String, rounds:List<String>){
        val workout = Workout(
            id = (System.currentTimeMillis() / 1000).toString(),
            title = title,
            type = type,
            description = description,
            time = time,
            number= number,
            dateTime = dateTime,
            rounds = rounds,
        ).toWorkoutEntity()

        viewModelScope.launch {
            room.dao.insertWorkout(workout)
        }
    }
}