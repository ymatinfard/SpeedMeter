package com.matin.speedmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.speedmeter.navigation.SpeedMeterNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpeedMeterTheme {
                SpeedMeterNavHost()
            }
        }
    }
}