@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.pandoro.ui.screens.groups.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.theme.AppTypography
import com.tecknobit.pandoro.ui.theme.EmptyStateTitleStyle
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
fun Groups(
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
        containerModifier = Modifier
            .fillMaxSize(),
        resource = Res.drawable.no_groups,
        resourceSize = responsiveAssignment(
            onExpandedSizeClass = { 350.dp },
            onMediumSizeClass = { 300.dp },
            onCompactSizeClass = { 275.dp }
        ),
        contentDescription = "No groups available",
        title = stringResource(Res.string.no_groups_available),
        titleStyle = EmptyStateTitleStyle
    )
}