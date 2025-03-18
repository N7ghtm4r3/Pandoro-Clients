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
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid

// TODO: TO COMMENT

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

@Composable
@NonRestartableComposable
private fun NoGroupsAvailable() {
    // TODO: TO TO
}