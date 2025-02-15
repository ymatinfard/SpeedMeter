package com.matin.speedmeter

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import com.matin.sync.Sync
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpeedMeterApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Sync.init(this)

        setStrictModePolicy()
    }

    private fun isDebuggable(): Boolean {
        return applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    /**
     * Set a thread policy that detects all potential problems on the main thread, such as network
     * and disk access.
     *
     * If a problem is found, the offending call will be logged and the application will be killed.
     */
    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }
}
