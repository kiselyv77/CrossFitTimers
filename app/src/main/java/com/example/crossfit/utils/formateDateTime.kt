package com.example.crossfit.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun String.formateDateTime():String{

    val format = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
    val date = LocalDateTime.parse(this, format)
    val month = date.month.toString()
    val day = date.dayOfMonth
    val time = "${date.hour}:${date.minute}"

    val monthRus =  when(month.toLowerCase()){
        "january" ->  "Января"
        "february" -> "Февраля"
        "march"  ->  "Марта"
        "april"  -> "Апреля"
        "may"  ->  "Мая"
        "june"  ->  "Июня"
        "july"  -> "Июля"
        "august"  ->  "Августа"
        "september"  -> "Сентября"
        "october"  ->  "Октября"
        "november"  ->  "Ноября"
        "december"  ->  "Декабря"
        else -> "Error formateDateTime!"
    }
    return "$day $monthRus в $time"
}