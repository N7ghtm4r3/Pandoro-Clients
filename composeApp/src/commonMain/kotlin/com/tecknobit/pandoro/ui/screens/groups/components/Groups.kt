@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.pandoro.ui.screens.groups.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.theme.AppTypography
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.no_groups
import pandoro.composeapp.generated.resources.no_groups_available

/**
 * Custom layout used to display the current groups of the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
fun Groups(
    viewModel: GroupsScreenViewModel
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            GroupsGrid(
                viewModel = viewModel
            )
        },
        onMediumSizeClass = {
            GroupsGrid(
                viewModel = viewModel
            )
        },
        onCompactSizeClass = {
            GroupsList(
                viewModel = viewModel
            )
        }
    )
}

/**
 * Custom [PaginatedLazyVerticalGrid] used to display the current groups of the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
@ResponsiveClassComponent(
    classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
)
private fun GroupsGrid(
    viewModel: GroupsScreenViewModel
) {
    PaginatedLazyVerticalGrid(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.allGroupsState,
        columns = GridCells.Adaptive(
            minSize = 300.dp
        ),
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoGroupsAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        items(
            items = viewModel.allGroupsState.allItems!!,
            key = { group -> group.id }
        ) { group ->
            GroupCard(
                modifier = Modifier
                    .width(325.dp),
                viewModel = viewModel,
                group = group
            )
        }
    }
}

/**
 * Custom [PaginatedLazyColumn] used to display the current groups of the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@CompactClassComponent
@NonRestartableComposable
private fun GroupsList(
    viewModel: GroupsScreenViewModel
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.allGroupsState,
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageEmptyIndicator = { NoGroupsAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        items(
            items = viewModel.allGroupsState.allItems!!,
            key = { group -> group.id }
        ) { group ->
            GroupCard(
                modifier = Modifier
                    .fillMaxWidth(),
                viewModel = viewModel,
                group = group
            )
        }
    }
}

/**
 * Empty state layout displayed when there are no groups available
 */
@Composable
@NonRestartableComposable
private fun NoGroupsAvailable() {
    EmptyState(
        resource = Res.drawable.no_groups,
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 275.dp }
        ),
        contentDescription = "No groups available",
        title = stringResource(Res.string.no_groups_available),
        titleStyle = AppTypography.bodyLarge
    )
}