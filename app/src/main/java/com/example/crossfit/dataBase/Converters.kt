package com.example.crossfit.dataBase

import androidx.room.TypeConverter
import com.example.crossfit.models.WorkoutType
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun saveDoubleList(listOfString: List<String>): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun getDoubleList(listOfString: String): List<String> {
        return Gson().fromJson(
            listOfString.toString(),
            object : TypeToken<List<String?>?>() {}.type
        )
    }

}