@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.pandoro.ui.screens.notes.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import com.tecknobit.pandoro.ui.theme.AppTypography
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.no_notes
import pandoro.composeapp.generated.resources.no_notes_available

/**
 * Custom layout used to display the current notes of the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
fun Notes(
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
                    .fillMaxWidth()
                    .height(175.dp),
                viewModel = viewModel,
                note = note
            )
        }
    }
}

/**
 * Empty state layout displayed when there are no notes available
 */
@Composable
private fun NoNotesAvailable() {
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resource = Res.drawable.no_notes,
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 275.dp }
        ),
        contentDescription = "No notes available",
        title = stringResource(Res.string.no_notes_available),
        titleStyle = AppTypography.bodyLarge
    )
}