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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.*
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
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

/**
 * The [CreateScreen] serves as a base screen to create a new item or edit an existing one
 *
 * @param itemId The identifier of the item to edit
 * @param viewModel The support viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
 * @see PandoroScreen
 */
@Structure
@ScreenCoordinator
abstract class CreateScreen<I, V : EquinoxViewModel>(
    itemId: String?,
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    /**
     * `isEditing` -> whether the user is creating or editing an item
     */
    protected val isEditing: Boolean = itemId != null

    /**
     * `item` -> state flow holds the item data if [isEditing] is true
     */
    protected lateinit var item: State<I?>

    /**
     * `fullScreenFormType` -> state holds the type of the form to use to create or edit the [item]
     */
    protected lateinit var fullScreenFormType: MutableState<Boolean>

    /**
     * Container component to safely display the content of the screen when the [item] is not null
     *
     * @param creationTitle The title of the screen when creating an item
     * @param editingTitle The tile of the screen when editing an item
     * @param subTitle Custom subtitle
     * @param initializationProcedure The procedure to initialize the [item]
     */
    @Composable
    protected fun LoadAwareContent(
        creationTitle: StringResource,
        editingTitle: StringResource,
        subTitle: @Composable (() -> Unit)? = null,
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
                        editingTitle = editingTitle,
                        subTitle = subTitle
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
        subTitle: @Composable (() -> Unit)? = null
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
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
                    creationTitle,
                subTitle = subTitle
            ) {
                Form()
            }
        }
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun FabAction()

    /**
     * Form to allow the creation or the editing of the [item]
     */
    @Composable
    @NonRestartableComposable
    protected fun Form() {
        ResponsiveContent(
            onExpandedSizeClass = { CardForm() },
            onMediumSizeClass = { CardForm() },
            onCompactSizeClass = { FullScreenForm() }
        )
    }

    /**
     * [Form] displayed as card
     */
    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    protected open fun CardForm() {
        fullScreenFormType.value = false
    }

    /**
     * [Form] displayed as full screen object, this is used for example in the mobile devices
     */
    @Composable
    @RequiresSuperCall
    @CompactClassComponent
    @NonRestartableComposable
    protected open fun FullScreenForm() {
        fullScreenFormType.value = true
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        fullScreenFormType = remember { mutableStateOf(false) }
    }

    /**
     * Picker of the images such icons or logos
     *
     * @param modifier The modifier to apply to the component
     * @param pickerSize The size of the picker
     * @param imageData The data of the image currently picked
     * @param contentDescription The description of the content displayed by the thumbnail
     * @param onImagePicked The action to execute when the image has been picked
     */
    @Composable
    @NonRestartableComposable
    protected fun ImagePicker(
        modifier: Modifier = Modifier,
        pickerSize: Dp,
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
                    size = pickerSize,
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

    /**
     * Custom button to save the data inserted in the [Form]
     *
     * @param modifier The modifier to apply to the component
     * @param textStyle The style to apply to the text
     * @param shape The shape of the button
     * @param onClick The action to execute when the button has been clicked
     */
    @Composable
    @NonRestartableComposable
    protected fun SaveButton(
        modifier: Modifier = Modifier,
        textStyle: TextStyle = TextStyle.Default,
        shape: Shape = ButtonDefaults.shape,
        onClick: () -> Unit
    ) {
        val localSoftInputKeyboard = LocalSoftwareKeyboardController.current
        Button(
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            shape = shape,
            onClick = {
                localSoftInputKeyboard?.hide()
                onClick.invoke()
            }
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