@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.projects.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.projects.components.FilterProjects
import com.tecknobit.pandoro.ui.screens.projects.components.InDevelopmentProjectCard
import com.tecknobit.pandoro.ui.screens.projects.components.ProjectCard
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.screens.ListsScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.empty_filtered_projects
import pandoro.composeapp.generated.resources.in_development
import pandoro.composeapp.generated.resources.no_projects_available
import pandoro.composeapp.generated.resources.projects

class ProjectsScreen: ListsScreen<ProjectsScreenViewModel>(
    viewModel = ProjectsScreenViewModel(),
    screenTitle = Res.string.projects
) {

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = { navigator.navigate(CREATE_PROJECT_SCREEN) }
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
        var projectsAvailable by remember { mutableStateOf(false) }
        if(projectsAvailable || viewModel!!.inDevelopmentProjectsFilter.value.isNotEmpty()) {
            SectionHeader(
                header = Res.string.in_development,
                isAllItemsFiltering = false
            )
        }
        PaginatedLazyRow(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel!!.inDevelopmentProjectState,
            contentPadding = PaddingValues(
                vertical = if(projectsAvailable)
                    10.dp
                else
                    0.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            firstPageEmptyIndicator = {
                projectsAvailable = false
                if(viewModel!!.inDevelopmentProjectsFilter.value.isNotEmpty()) {
                    EmptyResultWithFilters(
                        info = Res.string.empty_filtered_projects
                    )
                }
            },
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
        ) {
            items(
                items = viewModel!!.inDevelopmentProjectState.allItems!!,
                key = { inDevelopmentProject -> inDevelopmentProject.update.id }
            ) { inDevelopmentProject ->
                projectsAvailable = true
                InDevelopmentProjectCard(
                    inDevelopmentProject = inDevelopmentProject
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
            Compact -> {
                PaginatedLazyColumn(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.projectsState,
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    firstPageEmptyIndicator = {
                        NoDataAvailable(
                            icon = Icons.Default.FolderOff,
                            subText = Res.string.no_projects_available
                        )
                    },
                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
                    newPageProgressIndicator = { NewPageProgressIndicator() }
                ) {
                    items(
                        items = viewModel!!.projectsState.allItems!!,
                        key = { project -> project.id }
                    ) { project ->
                        ProjectCard(
                            viewModel = viewModel!!,
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
            else -> {
                PaginatedLazyVerticalGrid(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.projectsState,
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
                            icon = Icons.Default.FolderOff,
                            subText = Res.string.no_projects_available
                        )
                    },
                    firstPageProgressIndicator = { FirstPageProgressIndicator() },
                    newPageProgressIndicator = { NewPageProgressIndicator() }
                ) {
                    items(
                        items = viewModel!!.projectsState.allItems!!,
                        key = { project -> project.id }
                    ) { project ->
                        ProjectCard(
                            viewModel = viewModel!!,
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
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FilterRowItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterProjects(
            show = show,
            viewModel = viewModel!!,
            isAllProjectsFiltering = false
        )
    }

    @Composable
    @NonRestartableComposable
    override fun FilterAllItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterProjects(
            show = show,
            viewModel = viewModel!!,
            isAllProjectsFiltering = true
        )
    }

    override fun onCreate() {
        viewModel!!.setActiveContext(HomeScreen::class.java)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.inDevelopmentProjectsFilter = remember { mutableStateOf("") }
        viewModel!!.projectsFilter = remember { mutableStateOf("") }
    }

}