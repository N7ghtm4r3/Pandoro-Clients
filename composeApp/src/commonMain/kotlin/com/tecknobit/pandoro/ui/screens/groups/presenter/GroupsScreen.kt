package com.tecknobit.pandoro.ui.screens.groups.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.CREATE_GROUP_SCREEN
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.groups.components.GroupCard
import com.tecknobit.pandoro.ui.screens.groups.components.MyGroupCard
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.screens.ListsScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.empty_filtered_groups
import pandoro.composeapp.generated.resources.groups
import pandoro.composeapp.generated.resources.my_groups
import pandoro.composeapp.generated.resources.no_groups_available

class GroupsScreen : ListsScreen<GroupsScreenViewModel>(
    viewModel = GroupsScreenViewModel(),
    screenTitle = Res.string.groups
) {

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = { navigator.navigate(CREATE_GROUP_SCREEN) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ItemsInRow() {
        var groupsAvailable by remember { mutableStateOf(false) }
        if(groupsAvailable || viewModel!!.myGroupsStateFilters.value.isNotEmpty()) {
            SectionHeader(
                header = Res.string.my_groups,
                isAllItemsFiltering = false
            )
        }
        PaginatedLazyRow(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel!!.myGroupsState,
            contentPadding = PaddingValues(
                vertical = if (groupsAvailable)
                    10.dp
                else
                    0.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            firstPageEmptyIndicator = {
                groupsAvailable = false
                if(viewModel!!.myGroupsStateFilters.value.isNotEmpty()) {
                    EmptyResultWithFilters(
                        info = Res.string.empty_filtered_groups
                    )
                }
            }
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
                items = viewModel!!.myGroupsState.allItems!!,
                key = { group -> group.id }
            ) { group ->
                groupsAvailable = true
                MyGroupCard(
                    viewModel = viewModel!!,
                    group = group
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ItemsAdaptedSize() {
        SectionHeader(
            header = Res.string.all,
            isAllItemsFiltering = true
        )
        val windowWidthSizeClass = getCurrentWidthSizeClass()
        when(windowWidthSizeClass) {
            Expanded, Medium -> {
                PaginatedLazyVerticalGrid(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.allGroupsState,
                    columns = GridCells.Adaptive(
                        minSize = 300.dp
                    ),
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = {
                        NoDataAvailable(
                            icon = Icons.Default.Groups3,
                            subText = Res.string.no_groups_available,
                        )
                    }
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
                        items = viewModel!!.allGroupsState.allItems!!,
                        key = { group -> group.id }
                    ) { group ->
                        GroupCard(
                            modifier = Modifier
                                .width(325.dp),
                            viewModel = viewModel!!,
                            group = group
                        )
                    }
                }
            } else -> {
                PaginatedLazyColumn(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.allGroupsState,
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = {
                        NoDataAvailable(
                            icon = Icons.Default.Groups3,
                            subText = Res.string.no_groups_available,
                        )
                    }
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
                        items = viewModel!!.allGroupsState.allItems!!,
                        key = { group -> group.id }
                    ) { group ->
                        GroupCard(
                            modifier = Modifier
                                .fillMaxWidth(),
                            viewModel = viewModel!!,
                            group = group
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FilterRowItemsUi(
        show: MutableState<Boolean>
    ) {
    }

    @Composable
    @NonRestartableComposable
    override fun FilterAllItemsUi(
        show: MutableState<Boolean>
    ) {
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.myGroupsStateFilters = remember { mutableStateOf("") }
        viewModel!!.allGroupsStateFilters = remember { mutableStateOf("") }
    }

}