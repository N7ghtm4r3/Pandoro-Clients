package com.tecknobit.pandoro

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import kotlinx.coroutines.delay

/**
 * Function to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    // TODO: MAKE THE REAL NAVIGATION
    LaunchedEffect(Unit) {
        delay(1000)
        navigator.navigate(HOME_SCREEN)
    }
}

/**
 * Function to get the current screen dimension of the device where the application is running
 *
 *
 * @return the width size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
@ExperimentalMaterial3WindowSizeClassApi
actual fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return calculateWindowSizeClass().widthSizeClass
}