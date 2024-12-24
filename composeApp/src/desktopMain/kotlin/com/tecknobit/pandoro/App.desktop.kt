package com.tecknobit.pandoro

import OctocatKDUConfig
import UpdaterDialog
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import io.github.vinceglb.filekit.core.PlatformFile
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.app_name
import pandoro.composeapp.generated.resources.app_version
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.Locale

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    var launchApp by remember { mutableStateOf(true) }
    UpdaterDialog(
        config = OctocatKDUConfig(
            locale = Locale.getDefault(),
            appName = stringResource(Res.string.app_name),
            currentVersion = stringResource(Res.string.app_version),
            onUpdateAvailable = { launchApp = false },
            dismissAction = { launchApp = true }
        )
    )
    if (launchApp)
        startSession()
}

/**
 * Method to get the current screen dimension of the device where the application is running
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
 * Method to get the image picture's path
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

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    var tag = localUser.language
    if (tag == null)
        tag = DEFAULT_LANGUAGE
    Locale.setDefault(Locale.forLanguageTag(tag))
}