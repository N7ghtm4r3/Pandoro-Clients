package com.tecknobit.pandoro.ui.screens.notes.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.icons.ClipboardList
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.notes.component.NoteCard
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.no_notes_available
import pandoro.composeapp.generated.resources.notes
import pandoro.composeapp.generated.resources.retry_to_reconnect

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
                            onClick = {
                                // TODO: ADD NOTE 
                            }
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
                       Notes()
                    }
                }
            },
            serverOfflineRetryText = stringResource(Res.string.retry_to_reconnect),
            serverOfflineRetryAction = { viewModel!!.notesState.retryLastFailedRequest() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Notes() {
        val widthSizeClass = getCurrentWidthSizeClass()
        when(widthSizeClass) {
            Expanded, Medium -> {
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
                    firstPageEmptyIndicator = { NoNotesAvailable() }
                    // TODO: TO SET
                    /*firstPageProgressIndicator = { ... },
                    newPageProgressIndicator = { ... },*/
                    /*firstPageErrorIndicator = { e -> // from setError
                        ... e.message ...
                        ... onRetry = { paginationState.retryLastFailedRequest() } ...
                    },
                    newPageErrorIndicator = { e -> ... },
                    // The rest of LazyColumn params*/
                ) {
                    items(
                        items = viewModel!!.notesState.allItems!!,
                        key = { note -> note.id }
                    ) { note ->
                        NoteCard(
                            note = note
                        )
                    }
                }
            }
            else -> {
                PaginatedLazyColumn(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.notesState,
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = { NoNotesAvailable() }
                    // TODO: TO SET
                    /*firstPageProgressIndicator = { ... },
                    newPageProgressIndicator = { ... },*/
                    /*firstPageErrorIndicator = { e -> // from setError
                        ... e.message ...
                        ... onRetry = { paginationState.retryLastFailedRequest() } ...
                    },
                    newPageErrorIndicator = { e -> ... },
                    // The rest of LazyColumn params*/
                ) {
                    items(
                        items = viewModel!!.notesState.allItems!!,
                        key = { note -> note.id }
                    ) { note ->
                        NoteCard(
                            note = note
                        )
                    }
                }
            }
        }
    }

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
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}