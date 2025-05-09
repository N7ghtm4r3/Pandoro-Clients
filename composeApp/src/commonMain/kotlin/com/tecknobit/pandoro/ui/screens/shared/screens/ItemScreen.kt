@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit

/**
 * The [ItemScreen] serves as a base screen to display the data related to an item
 *
 * @param viewModel The support viewmodel of the screen
 * @param bottomPadding The value of the bottom padding to use
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
 * @see PandoroScreen
 */
@Structure
@ScreenCoordinator
abstract class ItemScreen<I, V: EquinoxViewModel>(
    viewModel: V,
    private val bottomPadding: Dp = 16.dp
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    /**
     * `item` -> state flow holds the item data
     */
    protected lateinit var item: State<I?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent()
    }

    /**
     * Container component to safely display the content of the screen when the [item] is not null
     */
    @Composable
    private fun LoadAwareContent() {
        PandoroTheme {
            AnimatedVisibility(
                visible = item.value != null,
                enter = fadeIn(),
                exit = fadeOut(),
                content = { ItemLoadedContent() }
            )
            AnimatedVisibility(
                visible = item.value == null,
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
    protected fun ItemLoadedContent() {
        ManagedContent(
            viewModel = viewModel,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
                    floatingActionButton = { FabAction() }
                ) {
                    PlaceContent(
                        paddingValues = PaddingValues(
                            top = 16.dp,
                            start = 0.dp,
                            end = 0.dp,
                            bottom = bottomPadding
                        ),
                        screenTitle = { ItemScreenTitle() },
                    ) {
                        ScreenContent()
                    }
                }
            }
        )
    }

    /**
     * The section of the title of the screen
     */
    @Composable
    @NonRestartableComposable
    private fun ItemScreenTitle() {
        Column(
            modifier = Modifier
                .widthIn(
                    max = FORM_CARD_WIDTH
                )
                .fillMaxWidth(),
        ) {
            ItemTitle()
            ItemInformation()
        }
    }

    /**
     * The title of the screen
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ItemTitle()

    /**
     * The item base information to display
     */
    @Composable
    @NonRestartableComposable
    protected fun ItemInformation() {
        ListItem(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    end = 16.dp
                )
                .clip(
                    RoundedCornerShape(
                        topEnd = 12.dp,
                        bottomEnd = 12.dp
                    )
                ),
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
            ),
            leadingContent = {
                val modalBottomSheetState = rememberModalBottomSheetState()
                val scope = rememberCoroutineScope()
                Thumbnail(
                    size = 100.dp,
                    thumbnailData = getThumbnailData(),
                    contentDescription = "Thumbnail of the Item",
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
                ItemDescription(
                    state = modalBottomSheetState,
                    scope = scope
                )
            },
            overlineContent = {
                ItemRelationshipItems()
            },
            headlineContent = {
                if(iAmTheAuthor())
                    ActionButtons()
                else
                    ItemAuthor()
            },
            trailingContent = {
                ExtraAction()
            }
        )
    }

    /**
     * Method to get the thumbnail data from the item
     *
     * @return the thumbnail data of the item as nullable [String]
     */
    protected abstract fun getThumbnailData() : String?

    /**
     * The related items of the [item] such groups or projects
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ItemRelationshipItems()

    /**
     * The description of the item dynamically displayed on a [ModalBottomSheet]
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
    @Composable
    @NonRestartableComposable
    private fun ItemDescription(
        state: SheetState,
        scope: CoroutineScope
    ) {
        if(state.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        state.hide()
                    }
                }
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 5.dp
                        ),
                    text = stringResource(Res.string.description),
                    textAlign = TextAlign.Center,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                Text(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .verticalScroll(rememberScrollState()),
                    text = getItemDescription(),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }

    /**
     * Method to get the description of the item
     *
     * @return the description of the item as [String]
     */
    protected abstract fun getItemDescription(): String

    /**
     * The available action can be executed on the [item] such editing or deleting it
     */
    @Composable
    @NonRestartableComposable
    private fun ActionButtons() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    onEdit()
                }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.edit),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    backgroundColor = MaterialTheme.colorScheme.primary
                )
            }
            val deleteProject = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { deleteProject.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            DeleteItemAction(
                delete = deleteProject
            )
        }
    }

    /**
     * The action to execute when the [item] has been edited
     */
    protected abstract fun onEdit()

    /**
     * The action to execute when the [item] has been requested to delete
     *
     * @param delete Whether the warn alert about the deletion is shown
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun DeleteItemAction(
        delete: MutableState<Boolean>
    )

    /**
     * The section to display the information of the [item] author
     */
    @Composable
    @NonRestartableComposable
    private fun ItemAuthor() {
        Column {
            val author = getItemAuthor()
            Text(
                text = author.completeName(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = displayFontFamily
            )
            Text(
                text = author.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }

    /**
     * Method to check whether the [localUser] is the author of the [item]
     *
     * @return whether the [localUser] is the author of the [item] as [Boolean]
     */
    protected fun iAmTheAuthor() : Boolean {
        return getItemAuthor().id == localUser.userId
    }

    /**
     * Method to get the author of the [item]
     *
     * @return the author of the item as [PandoroUser]
     */
    protected abstract fun getItemAuthor(): PandoroUser

    /**
     * Extra action component to allow the user to execute an action on the [item] such the
     * leaving from a group
     */
    @Composable
    @NonRestartableComposable
    protected open fun ExtraAction() {}

    /**
     * The related content of the screen
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ScreenContent()

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun FabAction()

}