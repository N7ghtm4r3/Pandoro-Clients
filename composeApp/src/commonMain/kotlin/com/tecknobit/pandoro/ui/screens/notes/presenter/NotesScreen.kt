@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.notes.presenter

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.pandoro.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.RetryButton
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.notes.components.Notes
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.theme.fallbackColor
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.completed
import pandoro.composeapp.generated.resources.notes
import pandoro.composeapp.generated.resources.status
import pandoro.composeapp.generated.resources.to_do

/**
 * The [NotesScreen] display the list of the notes owned by the [com.tecknobit.pandoro.localUser]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 */
class NotesScreen: PandoroScreen<NotesScreenViewModel>(
    viewModel = NotesScreenViewModel()
) {

    /**
     * The custom content of the screen
     */
    @Composable
    override fun ColumnScope.ScreenContent() {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = viewModel.sessionFlowState,
            statusContainerColor = MaterialTheme.colorScheme.primary,
            fallbackContentColor = fallbackColor(),
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
                    }
                ) {
                    Column {
                        Filters()
                        Notes(
                            viewModel = viewModel
                        )
                    }
                }
            },
            retryFailedFlowContent = {
                RetryButton(
                    onRetry = {
                        viewModel.notesState.retryLastFailedRequest()
                    }
                )
            }
        )
    }

    /**
     * The section where is displayed the title of the current screen
     */
    @Composable
    @ScreenSection
    override fun TitleSection() {
        ScreenTitle(
            title = Res.string.notes
        )
    }

    /**
     * The filters applicable to the notes list
     */
    @Composable
    @ScreenSection
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
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}