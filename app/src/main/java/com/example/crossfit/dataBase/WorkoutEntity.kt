package com.example.crossfit.dataBase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.crossfit.models.WorkoutType

@Entity
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val time: String,
    val number:String,
    val type: String,
    val dateTime:String,
    val rounds: List<String>
)
