package com.matin.feature.stopwatch.managers

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class AppLifeCycleObserver(
    private val onAppBackground: () -> Unit,
    private val onAppForeground: () -> Unit
) : DefaultLifecycleObserver {

    override fun onStart(owner: LifecycleOwner) {
        onAppForeground()
    }

    override fun onStop(owner: LifecycleOwner) {
        onAppBackground()
    }
}