package com.tecknobit.pandoro.ui.screens.project.components

import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import com.tecknobit.equinoxcompose.helpers.utils.ContextActivityProvider

/**
 * Method to prevent the screen sleeps when reading the change notes of an update
 */
actual fun preventScreenSleep() {
    val activity = ContextActivityProvider.getCurrentActivity()!!
    activity.window.addFlags(FLAG_KEEP_SCREEN_ON)
}

/**
 * Method to allow the screen sleeps when finished reading the change notes of an update
 */
actual fun allowsScreenSleep() {
    val activity = ContextActivityProvider.getCurrentActivity()!!
    activity.window.clearFlags(FLAG_KEEP_SCREEN_ON)
}