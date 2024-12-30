package com.tecknobit.pandoro.ui.screens.notes.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.icons.ClipboardList
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.notes.components.NoteCard
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.completed
import pandoro.composeapp.generated.resources.no_notes_available
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
            viewModel = viewModel!!,
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
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
                        Notes()
                    }
                }
            },
            serverOfflineRetryText = stringResource(Res.string.retry_to_reconnect),
            serverOfflineRetryAction = { viewModel!!.notesState.retryLastFailedRequest() }
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
                                    checked = viewModel!!.selectToDoNotes.value,
                                    onCheckedChange = { viewModel!!.manageToDoNotesFilter() }
                                )
                            }
                        }
                    },
                    onClick = { viewModel!!.manageToDoNotesFilter() }
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
                                checked = viewModel!!.selectCompletedNotes.value,
                                onCheckedChange = { viewModel!!.manageCompletedNotesFilter() }
                            )
                        }
                    },
                    onClick = { viewModel!!.manageCompletedNotesFilter() }
                )
            }
        }
    }

    /**
     * The notes section dynamically adapted to the screen sizes
     */
    @Composable
    @NonRestartableComposable
    private fun Notes() {
        val widthSizeClass = getCurrentWidthSizeClass()
        when(widthSizeClass) {
            Compact -> {
                PaginatedLazyColumn(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.notesState,
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = { NoNotesAvailable() },
                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
                    newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
                ) {
                    items(
                        items = viewModel!!.notesState.allItems!!,
                        key = { note -> note.id }
                    ) { note ->
                        NoteCard(
                            modifier = Modifier
                                .height(
                                    height = 175.dp
                                ),
                            viewModel = viewModel!!,
                            note = note
                        )
                    }
                }
            }
            else -> {
                PaginatedLazyVerticalGrid(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.notesState,
                    columns = GridCells.Adaptive(
                        minSize = 300.dp
                    ),
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = { NoNotesAvailable() },
                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
                    newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
                ) {
                    items(
                        items = viewModel!!.notesState.allItems!!,
                        key = { note -> note.id }
                    ) { note ->
                        NoteCard(
                            modifier = Modifier
                                .size(
                                    width = 300.dp,
                                    height = 175.dp
                                ),
                            viewModel = viewModel!!,
                            note = note
                        )
                    }
                }
            }
        }
    }

    /**
     * The UI to display when no notes are available to be displayed
     */
    @Composable
    @NonRestartableComposable
    private fun NoNotesAvailable() {
        EmptyListUI(
            icon = ClipboardList,
            subText = Res.string.no_notes_available,
            textStyle = TextStyle(
                fontFamily = bodyFontFamily
            ),
            themeColor = MaterialTheme.colorScheme.inversePrimary
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been created
     */
    override fun onCreate() {
        viewModel!!.setActiveContext(HomeScreen::class.java)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.selectToDoNotes = remember { mutableStateOf(true) }
        viewModel!!.selectCompletedNotes = remember { mutableStateOf(true) }
    }

}