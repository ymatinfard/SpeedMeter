package com.matin.core.common

object TimeFormatter {
    fun formatTime(timeInMillis: Long): String {
        val minutes = (timeInMillis / 1000) / 60
        val seconds = (timeInMillis / 1000) % 60
        val millis = (timeInMillis % 1000) / 100
        return String.format("%02d:%02d.%02d", minutes, seconds, millis)
    }
}