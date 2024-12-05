package com.tecknobit.pandoro

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.delay
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

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
 * @return the size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
@ExperimentalMaterial3WindowSizeClassApi
actual fun getCurrentSizeClass(): WindowSizeClass {
    return calculateWindowSizeClass()
}

/**
 * Function to get the image picture's path
 *
 * @param imagePic: the asset from fetch its path
 *
 * @return the asset path as [String]
 */
actual fun getImagePath(
    imagePic: PlatformFile?
): String? {
    return imagePic?.path
}

/**
 * Method to copy to the clipboard a content value
 *
 * @param content The content to copy
 * @param onCopy The action to execute after the copy in the clipboard
 */
actual fun copyToClipboard(
    content: String,
    onCopy: () -> Unit
) {
    val stringSelection = StringSelection(content)
    Toolkit.getDefaultToolkit().systemClipboard.setContents(stringSelection, null)
    onCopy.invoke()
}