package com.tecknobit.pandoro.ui.screens.projects.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

@Composable
@NonRestartableComposable
fun Projects(
    viewModel: ProjectsScreenViewModel
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            ProjectsGrid(
                viewModel = viewModel
            )
        },
        onMediumSizeClass = {
            ProjectsGrid(
                viewModel = viewModel
            )
        },
        onCompactSizeClass = {
            ProjectsList(
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
private fun ProjectsGrid(
    viewModel: ProjectsScreenViewModel
) {
    PaginatedLazyVerticalGrid(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.projectsState,
        columns = GridCells.Adaptive(
            minSize = 300.dp
        ),
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoProjectsAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        items(
            items = viewModel.projectsState.allItems!!,
            key = { project -> project.id }
        ) { project ->
            ProjectCard(
                viewModel = viewModel,
                modifier = Modifier
                    .size(
                        width = 300.dp,
                        height = 200.dp
                    ),
                project = project
            )
        }
    }
}

@Composable
@CompactClassComponent
@NonRestartableComposable
private fun ProjectsList(
    viewModel: ProjectsScreenViewModel
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.projectsState,
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoProjectsAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        items(
            items = viewModel.projectsState.allItems!!,
            key = { project -> project.id }
        ) { project ->
            ProjectCard(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        height = 210.dp
                    ),
                project = project
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun NoProjectsAvailable() {
    // TODO: TO TO
}