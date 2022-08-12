package com.example.crossfit.models

data class Workout(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val time: String = "",
    val number:String = "",
    val type: String = "",
    val dateTime:String = "",
    val rounds: List<String> = emptyList()
)


