package com.example.crossfit.dataBase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WorkoutEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WorkoutDataBase: RoomDatabase() {
    abstract val dao: WorkoutDao
}