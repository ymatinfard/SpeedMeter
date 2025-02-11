package com.matin.core.common

import android.os.SystemClock
import javax.inject.Inject


class TimeProvider @Inject constructor() {
    fun elapsedRealtime(): Long = SystemClock.elapsedRealtime()
}