package com.tecknobit.pandoro.ui.screens.notes.presenter

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.pandoro.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.notes.components.Notes
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.completed
import pandoro.composeapp.generated.resources.notes
import pandoro.composeapp.generated.resources.retry_to_reconnect
import pandoro.composeapp.generated.resources.status
import pandoro.composeapp.generated.resources.to_do

/**
 * The [NotesScreen] display the list of the notes owned by the [com.tecknobit.pandoro.localUser]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see PandoroScreen
 */
class NotesScreen: PandoroScreen<NotesScreenViewModel>(
    viewModel = NotesScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel,
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { navigator.navigate(CREATE_NOTE_SCREEN) }
                        ) {
                            Icon(
                                imageVector = AddNotes,
                                contentDescription = null
                            )
                        }
                    },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = Res.string.notes
                    ) {
                        Filters()
                        Notes(
                            viewModel = viewModel
                        )
                    }
                }
            },
            serverOfflineRetryText = Res.string.retry_to_reconnect,
            serverOfflineRetryAction = { viewModel.notesState.retryLastFailedRequest() }
        )
    }

    /**
     * The filters applicable to the notes list
     */
    @Composable
    @NonRestartableComposable
    private fun Filters() {
        Row (
            modifier = Modifier
                .width(150.dp)
                .padding(
                    vertical = 5.dp
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = RoundedCornerShape(
                        size = 10.dp
                    )
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 15.dp
                    )
                    .weight(1f),
                textAlign = TextAlign.Left,
                text = stringResource(Res.string.status)
            )
            var menuOpened by remember { mutableStateOf(false) }
            IconButton(
                onClick = { menuOpened = !menuOpened }
            ) {
                Icon(
                    imageVector = if(menuOpened)
                        Icons.Default.KeyboardArrowUp
                    else
                        Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = menuOpened,
                onDismissRequest = { menuOpened = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.to_do)
                            )
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Checkbox(
                                    checked = viewModel.selectToDoNotes.value,
                                    onCheckedChange = { viewModel.manageToDoNotesFilter() }
                                )
                            }
                        }
                    },
                    onClick = { viewModel.manageToDoNotesFilter() }
                )
                DropdownMenuItem(
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.completed)
                            )
                            Checkbox(
                                checked = viewModel.selectCompletedNotes.value,
                                onCheckedChange = { viewModel.manageCompletedNotesFilter() }
                            )
                        }
                    },
                    onClick = { viewModel.manageCompletedNotesFilter() }
                )
            }
        }
    }

    /**
     * Method invoked when the [ShowContent] composable has been created
     */
    override fun onCreate() {
        viewModel.setActiveContext(HomeScreen::class)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.selectToDoNotes = remember { mutableStateOf(true) }
        viewModel.selectCompletedNotes = remember { mutableStateOf(true) }
    }

}