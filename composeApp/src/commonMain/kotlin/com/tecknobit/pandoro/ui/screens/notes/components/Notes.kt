package com.tecknobit.pandoro.ui.screens.notes.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

@Composable
@NonRestartableComposable
fun Notes(
    viewModel: NotesScreenViewModel
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            NotesGrid(
                viewModel = viewModel
            )
        },
        onMediumSizeClass = {
            NotesGrid(
                viewModel = viewModel
            )
        },
        onCompactSizeClass = {
            NotesList(
                viewModel = viewModel
            )
        }
    )
}

@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun NotesGrid(
    viewModel: NotesScreenViewModel
) {
    PaginatedLazyVerticalGrid(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.notesState,
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
            items = viewModel.notesState.allItems!!,
            key = { note -> note.id }
        ) { note ->
            NoteCard(
                modifier = Modifier
                    .size(
                        width = 300.dp,
                        height = 175.dp
                    ),
                viewModel = viewModel,
                note = note
            )
        }
    }
}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun NotesList(
    viewModel: NotesScreenViewModel
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.notesState,
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoNotesAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
    ) {
        items(
            items = viewModel.notesState.allItems!!,
            key = { note -> note.id }
        ) { note ->
            NoteCard(
                modifier = Modifier
                    .height(
                        height = 175.dp
                    ),
                viewModel = viewModel,
                note = note
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun NoNotesAvailable() {
    // TODO: TO TO
}