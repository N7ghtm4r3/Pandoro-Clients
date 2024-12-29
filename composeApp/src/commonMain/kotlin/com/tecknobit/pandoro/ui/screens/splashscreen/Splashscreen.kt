package com.tecknobit.pandoro.ui.screens.splashscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.CheckForUpdatesAndLaunch
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.app_name

/**
 * The [Splashscreen] class is used to retrieve and load the session data and enter the application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class Splashscreen : EquinoxScreen<EquinoxViewModel>() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        PandoroTheme {
            val textColor = contentColorFor(
                backgroundColor = MaterialTheme.colorScheme.primary
            )
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxSize()
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(Res.string.app_name),
                    fontFamily = displayFontFamily,
                    color = textColor,
                    fontSize = 55.sp,
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .padding(25.dp),
                        color = textColor,
                        text = "by Tecknobit"
                    )
                }
            }
            CheckForUpdatesAndLaunch()
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}