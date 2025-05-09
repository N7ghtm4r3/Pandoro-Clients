@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.pandoro.ui.screens.projects.components

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
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import com.tecknobit.pandoro.ui.theme.AppTypography
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.no_projects
import pandoro.composeapp.generated.resources.no_projects_available

/**
 * Custom layout used to display the current projects of the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
fun Projects(
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
                    .fillMaxWidth()
                    .height(
                        height = responsiveAssignment(
                            onExpandedSizeClass = { 200.dp },
                            onMediumSizeClass = { 200.dp },
                            onCompactSizeClass = { 210.dp }
                        )
                    ),
                project = project
            )
        }
    }
}

/**
 * Empty state layout displayed when there are no projects available
 */
@Composable
private fun NoProjectsAvailable() {
    EmptyState(
        containerModifier = Modifier
            .fillMaxSize(),
        resource = Res.drawable.no_projects,
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 325.dp },
            onMediumSizeClass = { 275.dp },
            onCompactSizeClass = { 250.dp }
        ),
        contentDescription = "No projects available",
        title = stringResource(Res.string.no_projects_available),
        titleStyle = AppTypography.bodyLarge
    )
}