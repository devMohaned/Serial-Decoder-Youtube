package com.serial.decoder.core.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serial.decoder.core.nav.NavigationHost
import com.serial.decoder.ui.theme.SerialDecoderTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            SerialDecoderApp(windowWidthSizeClass = windowSizeClass.widthSizeClass)
        }
    }
}


@Composable
fun SerialDecoderApp(modifier: Modifier = Modifier, windowWidthSizeClass: WindowWidthSizeClass) {
    SerialDecoderTheme {
        NavigationHost(modifier = modifier, windowSize = windowWidthSizeClass)
    }
}