@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.create

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
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
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.helpers.navigator
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.screens.shared.presenters.SessionFlowStateScreenConsumer
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
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
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see SessionFlowStateScreenConsumer
 */
@Structure
@ScreenCoordinator
abstract class CreateScreen<I, V : EquinoxViewModel>(
    itemId: String?,
    private val creationTitle: StringResource,
    private val editingTitle: StringResource,
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
), SessionFlowStateScreenConsumer {

    /**
     * `isEditing` whether the user is creating or editing an item
     */
    protected val isEditing: Boolean = itemId != null

    /**
     * `item` state flow holds the item data if [isEditing] is true
     */
    protected lateinit var item: State<I?>

    /**
     * `fullScreenFormType` state holds the type of the form to use to create or edit the [item]
     */
    protected lateinit var fullScreenFormType: MutableState<Boolean>

    /**
     * Method used to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        PandoroTheme {
            SessionFlowContainer(
                modifier = Modifier
                    .fillMaxSize(),
                state = sessionFlowState(),
                initialLoadingRoutineDelay = 1000L,
                loadingRoutine = if(isEditing) {
                    {
                        item.value != null
                    }
                } else
                    null,
                content = {
                    CollectStatesAfterLoading()
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary)
                    ) {
                        TitleSection()
                        SubtitleSection()
                        ScreenContent()
                    }
                }
            )
        }
    }

    /**
     * The section where is displayed the title of the current screen
     */
    @Composable
    @ScreenSection
    override fun TitleSection() {
        ScreenTitle(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(
                    top = 16.dp
                ),
            navBackAction = { navigator.popBackStack() },
            title = if(isEditing)
                editingTitle
            else
                creationTitle
        )
    }

    /**
     * The custom content of the screen
     */
    @Composable
    override fun ColumnScope.ScreenContent() {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
            floatingActionButton = { FabAction() }
        ) {
            Form()
        }
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    protected abstract fun FabAction()

    /**
     * Form to allow the creation or the editing of the [item]
     */
    @Composable
    @ScreenSection
    @LayoutCoordinator
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
    protected fun ImagePicker(
        modifier: Modifier = Modifier,
        pickerSize: Dp,
        imageData: String?,
        contentDescription: String,
        onImagePicked: (PlatformFile?) -> Unit
    ) {
        val launcher = rememberFilePickerLauncher(
            type = FileKitType.Image,
            mode = FileKitMode.Single,
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