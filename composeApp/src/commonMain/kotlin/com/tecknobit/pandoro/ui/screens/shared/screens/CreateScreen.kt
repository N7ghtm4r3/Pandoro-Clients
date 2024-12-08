package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.getCurrentSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.choose_the_icon_of_the_project
import pandoro.composeapp.generated.resources.edit
import pandoro.composeapp.generated.resources.save

@Structure
abstract class CreateScreen<I, V : EquinoxViewModel>(
    itemId: String?,
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    protected val FORM_CARD_WIDTH = 750.dp

    protected val FORM_CARD_HEIGHT = 550.dp

    protected val isEditing: Boolean = itemId != null

    protected lateinit var item: State<I?>

    protected lateinit var fullScreenFormType: MutableState<Boolean>

    @Composable
    @NonRestartableComposable
    // FIXME: (DEPRECATED TO TRIGGER SEARCH) REPLACE WITH THE REAL COMPONENT
    protected fun LoadAwareContent(
        creationTitle: StringResource,
        editingTitle: StringResource,
        initializationProcedure: @Composable () -> Unit
    ) {
        PandoroTheme {
            AnimatedVisibility(
                visible = (isEditing && item.value != null) || !isEditing,
                enter = fadeIn(),
                exit = fadeOut(),
                content = {
                    initializationProcedure.invoke()
                    ItemLoadedContent(
                        creationTitle = creationTitle,
                        editingTitle = editingTitle
                    )
                }
            )
            AnimatedVisibility(
                visible = isEditing && item.value == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(85.dp),
                            strokeWidth = 8.dp
                        )
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp
                                ),
                            text = stringResource(com.tecknobit.equinoxcompose.resources.Res.string.loading_data)
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ItemLoadedContent(
        creationTitle: StringResource,
        editingTitle: StringResource,
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
            floatingActionButton = { FabAction() }
        ) {
            PlaceContent(
                paddingValues = PaddingValues(
                    all = 0.dp
                ),
                titleModifier = Modifier
                    .padding(
                        top = 16.dp,
                        start = 16.dp
                    ),
                navBackAction = { navigator.goBack() },
                screenTitle = if(isEditing)
                    editingTitle
                else
                    creationTitle
            ) {
                Form()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun FabAction()

    @Composable
    @NonRestartableComposable
    protected fun Form() {
        val windowSizeClass = getCurrentSizeClass()
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass
        when {
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Expanded -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Medium -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Medium -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded -> {
                CardForm()
            }
            else -> FullScreenForm()
        }
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    protected open fun CardForm() {
        fullScreenFormType.value = false
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    protected open fun FullScreenForm() {
        fullScreenFormType.value = true
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        fullScreenFormType = remember { mutableStateOf(false) }
    }

    @Composable
    @NonRestartableComposable
    protected fun ImagePicker(
        modifier: Modifier = Modifier,
        iconSize: Dp,
        imageData: String?,
        contentDescription: String,
        onImagePicked: (PlatformFile?) -> Unit
    ) {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single,
            title = stringResource(Res.string.choose_the_icon_of_the_project)
        ) { image -> onImagePicked.invoke(image) }
        Column (
            modifier = modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Thumbnail(
                    modifier = Modifier
                        .align(Alignment.Center),
                    size = iconSize,
                    thumbnailData = imageData,
                    contentDescription = contentDescription
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xD0DFD8D8))
                        .size(40.dp),
                    onClick = { launcher.launch() }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun SaveButton(
        modifier: Modifier = Modifier,
        textStyle: TextStyle = TextStyle.Default,
        shape: Shape = ButtonDefaults.shape,
        onClick: () -> Unit
    ) {
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            shape = shape,
            onClick = onClick
        ) {
            Text(
                style = textStyle,
                text = stringResource(
                    if(isEditing)
                        Res.string.edit
                    else
                        Res.string.save
                )
            )
        }
    }

}