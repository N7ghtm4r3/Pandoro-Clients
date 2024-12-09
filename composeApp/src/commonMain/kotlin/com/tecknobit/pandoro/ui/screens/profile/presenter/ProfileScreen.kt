package com.tecknobit.pandoro.ui.screens.profile.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.profile

class ProfileScreen : PandoroScreen<ProfileScreenViewModel>(
    viewModel = ProfileScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = Res.string.profile
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            UserDetails()
                        }
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun UserDetails() {
        Row (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ProfilePicker()
            Text(
                text = localUser.completeName
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProfilePicker() {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single
        ) { imagePath ->
            val newProfilePic = getImagePath(
                imagePic = imagePath
            )
            newProfilePic?.let {
                viewModel!!.changeProfilePic(
                    imagePath = newProfilePic,
                    profilePic = viewModel!!.profilePic
                )
            }
        }
        Thumbnail(
            size = 125.dp,
            contentDescription = "Profile pic",
            thumbnailData = viewModel!!.profilePic.value,
            onClick = { launcher.launch() }
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.profilePic = remember {
            mutableStateOf(localUser.profilePic)
        }
    }

}