package com.jeluchu.jchuplayer.core.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity

fun msToTime(duration: Long): String {
    val milliseconds = ((duration % 1000) / 100)
    val seconds = ((duration / 1000) % 60).toInt()
    val minutes = ((duration / (1000 * 60)) % 60).toInt()
    val hours = ((duration / (1000 * 60 * 60)) % 24).toInt()

    val formattedHours = if (hours < 10)  "0$hours" else hours.toString()
    val formattedMinutes = if (minutes < 10) "0$minutes" else minutes.toString()
    val formattedSeconds = if (seconds < 10) "0$seconds" else seconds.toString()

    return if (formattedHours != "00") "$formattedHours:$formattedMinutes:$formattedSeconds"
    else "$formattedMinutes:$formattedSeconds"//$formattedSeconds.$milliseconds"
}

internal fun Context.findActivity(): Activity {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    throw IllegalStateException("Activity not found. Unknown error.")
}
