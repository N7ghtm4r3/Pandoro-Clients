@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit

@Structure
abstract class ItemScreen<I, V: EquinoxViewModel>(
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    protected lateinit var item: State<I?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent()
    }

    @Composable
    @NonRestartableComposable
    // FIXME: (DEPRECATED TO TRIGGER SEARCH) REPLACE WITH THE REAL COMPONENT
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
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    floatingActionButton = { FabAction() }
                ) {
                    PlaceContent(
                        paddingValues = PaddingValues(
                            top = 16.dp,
                            start = 6.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        screenTitle = { ItemScreenTitle() },
                    ) {
                        ScreenContent()
                    }
                }
            }
        )
    }

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

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemTitle()

    @Composable
    @NonRestartableComposable
    protected fun ItemInformation() {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                )
                .padding(
                    start = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            val modalBottomSheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            Thumbnail(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    ),
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
                modalBottomSheetState = modalBottomSheetState,
                scope = scope
            )
            Column {
                Column (
                    verticalArrangement = Arrangement.Center
                ) {
                    ItemRelationshipItems()
                }
                if(getItemAuthor().id == localUser.userId)
                    ActionButtons()
                else
                    ItemAuthor()
            }
        }
    }

    protected abstract fun getThumbnailData() : String?

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemRelationshipItems()

    @Composable
    @NonRestartableComposable
    private fun ItemDescription(
        modalBottomSheetState: SheetState,
        scope: CoroutineScope
    ) {
        if(modalBottomSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        modalBottomSheetState.hide()
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

    protected abstract fun getItemDescription(): String

    @Composable
    @NonRestartableComposable
    private fun ActionButtons() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
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
                    backgroundColor = MaterialTheme.colorScheme.inversePrimary
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
                    fontSize = 12.sp
                )
            }
            DeleteItemAction(
                delete = deleteProject
            )
        }
    }

    protected abstract fun onEdit()

    @Composable
    @NonRestartableComposable
    protected abstract fun DeleteItemAction(
        delete: MutableState<Boolean>
    )

    @Composable
    @NonRestartableComposable
    private fun ItemAuthor() {
        val author = getItemAuthor()
        Text(
            text = author.completeName(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = author.email,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }

    protected abstract fun getItemAuthor(): PandoroUser

    @Composable
    @NonRestartableComposable
    protected abstract fun ScreenContent()

    @Composable
    @NonRestartableComposable
    protected abstract fun FabAction()

}