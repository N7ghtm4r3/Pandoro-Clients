package com.tecknobit.pandoro.ui.screens.createproject.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.createproject.presentation.CreateProjectScreenViewModel
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.painterResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.create_project
import pandoro.composeapp.generated.resources.edit_project
import pandoro.composeapp.generated.resources.logo

class CreateProjectScreen(
    projectId: String?
) : PandoroScreen<CreateProjectScreenViewModel>(
    viewModel = CreateProjectScreenViewModel(
        projectId = projectId
    )
) {

    private val isEditing: Boolean = projectId != null

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        PandoroTheme {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.primary,
                snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                floatingActionButton = {

                }
            ) {
                PlaceContent(
                    navBackAction = { navigator.goBack() },
                    screenTitle = if(isEditing)
                        Res.string.edit_project
                    else
                        Res.string.create_project
                ) {
                    ProjectForm()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectForm() {
        val windowWidthSizeClass = getCurrentWidthSizeClass()
        when(windowWidthSizeClass) {
            Expanded -> ProjectCardForm()
            else -> FullScreenProjectForm()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectCardForm() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(
                        width = 750.dp,
                        height = 450.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .fillMaxSize()
                ) {
                    IconPicker(
                        iconSize = 120.dp
                    )
                    Column {
                        BasicTextField(
                            value = "",
                            onValueChange = {

                            }
                        )
                        EquinoxTextField(
                            value = remember { mutableStateOf("") },
                            label = ""
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun IconPicker(
        iconSize: Dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(iconSize)
                    .clip(CircleShape)
                    .align(Alignment.Center),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("icon")
                    .crossfade(true)
                    .crossfade(500)
                    .build(),
                // TODO: TO SET
                //imageLoader = imageLoader,
                contentDescription = "Project icon",
                contentScale = ContentScale.Crop,
                error = painterResource(Res.drawable.logo)
            )
            Button(
                modifier = Modifier
                    .width(iconSize)
                    .height(iconSize / 2),
                shape = RoundedCornerShape(
                    bottomStart = iconSize / 2,
                    bottomEnd = iconSize / 2,
                    topStart = 0.dp,
                    topEnd = 0.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                ),
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FullScreenProjectForm() {

    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}