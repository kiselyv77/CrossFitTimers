package com.example.crossfit.utils

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

fun Long.formateTime(): String {
    return String.format("%02d минут %02d секунд",
        TimeUnit.SECONDS.toMinutes(this),
        (this) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(this))
    )
}

fun Long.formateTime2(): String {
    return String.format("%02d:%02d",
        TimeUnit.SECONDS.toMinutes(this),
        (this) - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(this))
    )
}

