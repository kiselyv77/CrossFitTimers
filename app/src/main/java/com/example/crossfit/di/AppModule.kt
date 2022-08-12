package com.example.crossfit.di

import android.app.Application
import androidx.room.Room
import com.example.crossfit.dataBase.WorkoutDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoom(app: Application): WorkoutDataBase {
        return Room.databaseBuilder(app, WorkoutDataBase::class.java, "workoutDB").build()
    }
}