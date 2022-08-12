package com.example.crossfit.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface WorkoutDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(
        workoutDao: WorkoutEntity
    )

    @Query("SELECT * FROM workoutentity")
    suspend fun getWorkoutList(): List<WorkoutEntity>

    @Query("DELETE FROM workoutentity WHERE id = :id")
    suspend fun deleteId(id: String)

    @Query("SELECT * FROM workoutentity WHERE id=:id ")
    suspend fun getWorkoutById(id: String):WorkoutEntity

}