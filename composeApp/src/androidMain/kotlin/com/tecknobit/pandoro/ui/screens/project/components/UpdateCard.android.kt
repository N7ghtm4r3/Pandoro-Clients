package com.tecknobit.pandoro.ui.screens.project.components

import android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
import com.tecknobit.equinoxcompose.helpers.utils.ContextActivityProvider

actual fun preventScreenSleep() {
    val activity = ContextActivityProvider.getCurrentActivity()!!
    activity.window.addFlags(FLAG_KEEP_SCREEN_ON)
}

actual fun allowsScreenSleep() {
    val activity = ContextActivityProvider.getCurrentActivity()!!
    activity.window.clearFlags(FLAG_KEEP_SCREEN_ON)
}