package com.example.crossfit.fragments.mainSubfragments.fragmentHistory

import com.example.crossfit.models.Workout

data class HistoryFragmentState(
    val workoutList: List<Workout> = emptyList()
)
