package com.example.crossfit.models

import com.example.crossfit.dataBase.WorkoutEntity

fun Workout.toWorkoutEntity(): WorkoutEntity{
    return WorkoutEntity(
        id = id,
        title = this.title,
        description = description,
        time = time,
        number= number,
        type = type,
        dateTime = dateTime,
        rounds = rounds,
    )
}

fun WorkoutEntity.toWorkout(): Workout{
    return Workout(
        id = id,
        title = this.title,
        description = description,
        time = time,
        number= number,
        type = type,
        dateTime = dateTime,
        rounds = rounds,
    )
}