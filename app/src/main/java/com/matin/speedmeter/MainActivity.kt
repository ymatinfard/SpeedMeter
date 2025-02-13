package com.matin.speedmeter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.matin.core.designsystem.theme.SpeedMeterTheme
import com.matin.speedmeter.navigation.SpeedMeterNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(false) }
            SpeedMeterTheme(darkTheme = isDarkTheme) {
                SpeedMeterNavHost { isDark ->
                    isDarkTheme = isDark
                }

            }
        }
    }
}