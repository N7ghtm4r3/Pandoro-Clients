package com.tecknobit.pandoro

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.equinoxcompose.helpers.utils.AppContext
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.pandoro.MainActivity.Companion.appUpdateManager
import com.tecknobit.pandoro.MainActivity.Companion.launcher
import io.github.vinceglb.filekit.core.PlatformFile
import moe.tlaster.precompose.navigation.BackHandler
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.Locale
import kotlin.math.min

/**
 * Function to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
        val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
        val isUpdateSupported = info.isImmediateUpdateAllowed
        if (isUpdateAvailable && isUpdateSupported) {
            appUpdateManager.startUpdateFlowForResult(
                info,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } else
            startSession()
    }.addOnFailureListener {
        startSession()
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
    val activity = LocalContext.current as Activity
    return calculateWindowSizeClass(
        activity = activity
    )
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
    if (imagePic != null) {
        return getFilePath(
            context = AppContext.get(),
            uri = imagePic.uri
        )
    }
    return null
}

/**
 * Function to get the complete file path of an file
 *
 * @param context: the context where the file is needed
 * @param uri: the uri of the file
 * @return the path of the file as [String]
 */
private fun getFilePath(
    context: Context,
    uri: Uri
): String {
    val returnCursor = context.contentResolver.query(uri, null, null, null, null)
    val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
    returnCursor.moveToFirst()
    val name = returnCursor.getString(nameIndex)
    returnCursor.getLong(sizeIndex).toString()
    val file = File(context.filesDir, name)
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        var read = 0
        val maxBufferSize = 1 * 1024 * 1024
        val bytesAvailable: Int = inputStream?.available() ?: 0
        val bufferSize = min(bytesAvailable, maxBufferSize)
        val buffers = ByteArray(bufferSize)
        while (inputStream?.read(buffers).also {
                if (it != null) {
                    read = it
                }
            } != -1) {
            outputStream.write(buffers, 0, read)
        }
        inputStream?.close()
        outputStream.close()
    } catch (_: Exception) {
    } finally {
        returnCursor.close()
    }
    return file.path
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
    val context = AppContext.get()
    val clipboard = context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(null, content)
    clipboard.setPrimaryClip(clip)
    onCopy.invoke()
}

/**
 * Function to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
    val context = LocalContext.current as Activity
    BackHandler {
        context.finishAffinity()
    }
}

/**
 * Function to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    var tag = localUser.language
    if (tag == null)
        tag = DEFAULT_LANGUAGE
    val locale = Locale(tag)
    Locale.setDefault(locale)
    val context = AppContext.get()
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}