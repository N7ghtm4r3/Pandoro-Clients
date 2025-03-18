package com.tecknobit.pandoro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement.Maximized
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.tecknobit.equinoxcompose.session.setUpSession
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.app_name
import pandoro.composeapp.generated.resources.logo

/**
 * The [main] function is used as entry point of Nova's application for Desktop
 *
 * @author N7ghtm4r3 - Tecknobit
 */
fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = WindowState(
                placement = Maximized
            ),
            title = stringResource(Res.string.app_name),
            icon = painterResource(Res.drawable.logo)
        ) {
            setUpSession(
                hasBeenDisconnectedAction = {
                    localUser.clear()
                    navigator.navigate(AUTH_SCREEN)
                }
            )
            App()
        }
    }
}