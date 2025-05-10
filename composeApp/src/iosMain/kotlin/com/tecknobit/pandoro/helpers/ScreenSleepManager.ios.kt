package com.tecknobit.pandoro.helpers

import platform.UIKit.UIApplication

/**
 * Method to prevent the screen sleeps when reading the change notes of an update
 */
actual fun preventScreenSleep() {
    UIApplication.sharedApplication.idleTimerDisabled = true
}

/**
 * Method to allow the screen sleeps when finished reading the change notes of an update
 */
actual fun allowsScreenSleep() {
    UIApplication.sharedApplication.idleTimerDisabled = false
}