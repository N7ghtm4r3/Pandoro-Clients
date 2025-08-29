package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.pandoro.ui.screens.notes.components.ChangeNoteCard
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Update
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen.Companion.FORM_CARD_HEIGHT
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT

/**
 * Custom layout used to display the change notes of an [update]
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project owner of the update
 * @param update The update to display
 */
@Composable
@LayoutCoordinator
fun ChangeNotes(
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: Update
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            ChangeNotesGrid(
                viewModel = viewModel,
                project = project,
                update = update
            )
        },
        onMediumSizeClass = {
            ChangeNotesGrid(
                viewModel = viewModel,
                project = project,
                update = update
            )
        },
        onCompactSizeClass = {
            ChangeNotesList(
                viewModel = viewModel,
                project = project,
                update = update
            )
        }
    )
}

/**
 * Custom [LazyVerticalGrid] used to display the change notes of an [update]
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project owner of the update
 * @param update The update to display
 */
@Composable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun ChangeNotesGrid(
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: Update
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .heightIn(
                max = FORM_CARD_HEIGHT
            )
            .animateContentSize(),
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = update.notes,
            key = { note -> note.id }
        ) { note ->
            ChangeNoteCard(
                modifier = Modifier
                    .height(
                        height = 175.dp
                    ),
                viewModel = viewModel,
                update = update,
                project = project,
                note = note,
                allowedToChangeStatus = update.status == IN_DEVELOPMENT
            )
        }
    }
}

/**
 * Custom [LazyColumn] used to display the change notes of an [update]
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project owner of the update
 * @param update The update to display
 */
@Composable
@CompactClassComponent
private fun ChangeNotesList(
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: Update
) {
    LazyColumn(
        modifier = Modifier
            .heightIn(
                max = FORM_CARD_HEIGHT
            )
            .animateContentSize(),
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(
            items = update.notes,
            key = { note -> note.id }
        ) { note ->
            ChangeNoteCard(
                modifier = Modifier
                    .height(
                        height = 175.dp
                    ),
                viewModel = viewModel,
                project = project,
                update = update,
                note = note,
                allowedToChangeStatus = update.status == IN_DEVELOPMENT
            )
        }
    }
}