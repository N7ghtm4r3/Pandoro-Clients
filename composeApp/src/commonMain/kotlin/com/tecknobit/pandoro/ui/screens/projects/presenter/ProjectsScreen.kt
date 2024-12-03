@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.tecknobit.pandoro.ui.screens.projects.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.UIAnimations
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.projects.components.FilterProjects
import com.tecknobit.pandoro.ui.screens.projects.components.InDevelopmentProjectCard
import com.tecknobit.pandoro.ui.screens.projects.components.ProjectCard
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.empty_filtered_projects
import pandoro.composeapp.generated.resources.in_development
import pandoro.composeapp.generated.resources.no_projects_available
import pandoro.composeapp.generated.resources.projects
import pandoro.composeapp.generated.resources.retry_to_reconnect

class ProjectsScreen: PandoroScreen<ProjectsScreenViewModel>(
    viewModel = ProjectsScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    floatingActionButton = {
                        val createProject = remember { mutableStateOf(false) }
                        FloatingActionButton(
                            onClick = { createProject.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null
                            )
                        }
                        if(createProject.value)
                            NavToProjectCreation()
                    },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = Res.string.projects
                    ) {
                        ProjectsInDevelopmentSection()
                        ProjectsSection()
                    }
                }
            },
            serverOfflineRetryText = stringResource(Res.string.retry_to_reconnect),
            serverOfflineRetryAction = { viewModel!!.projectsState.retryLastFailedRequest() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun NavToProjectCreation(
        projectId: String? = null
    ) {
        //val windowWidthSizeClass = getCurrentWidthSizeClass()
        val projectIdPath = if(projectId != null)
            "/$projectId"
        else
            ""
        navigator.navigate("$CREATE_PROJECT_SCREEN$projectIdPath")
        /*when(windowWidthSizeClass) {
            Expanded -> navigator.navigate("$CREATE_PROJECT_DIALOG_SCREEN$projectIdPath")
            else -> navigator.navigate("$CREATE_PROJECT_SCREEN$projectIdPath")
        }*/
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsInDevelopmentSection() {
        var projectsAvailable by remember { mutableStateOf(false) }
        val projectsNotFound = !projectsAvailable && viewModel!!.inDevelopmentProjectsFilter.value.isNotEmpty()
        Column {
            if(projectsAvailable || viewModel!!.inDevelopmentProjectsFilter.value.isNotEmpty()) {
                SectionHeader(
                    header = Res.string.in_development
                )
            }
            if(projectsNotFound) {
                Text(
                    modifier = Modifier
                        .padding(
                            vertical = 10.dp
                        ),
                    text = stringResource(Res.string.empty_filtered_projects),
                    fontSize = 14.sp
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
                horizontalArrangement = Arrangement.spacedBy(10.dp)
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
                val inDevelopmentProjects = viewModel!!.inDevelopmentProjectState.allItems!!
                projectsAvailable = inDevelopmentProjects.isNotEmpty()
                if(projectsAvailable) {
                    items(
                        items = inDevelopmentProjects,
                        key = { inDevelopmentProject -> inDevelopmentProject.update.id }
                    ) { inDevelopmentProject ->
                        InDevelopmentProjectCard(
                            inDevelopmentProject = inDevelopmentProject
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsSection() {
        Column {
            SectionHeader(
                header = Res.string.all
            )
            ProjectsList()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsList() {
        val windowWidthSizeClass = getCurrentWidthSizeClass()
        var projectsAvailable by remember { mutableStateOf(false) }
        when(windowWidthSizeClass) {
            Expanded, Medium -> {
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val projects = viewModel!!.projectsState.allItems!!
                    projectsAvailable = projects.isNotEmpty()
                    if(projectsAvailable) {
                        items(
                            items = projects,
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
            } else -> {
                PaginatedLazyColumn(
                    modifier = Modifier
                        .animateContentSize(),
                    paginationState = viewModel!!.projectsState,
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
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
                    val projects = viewModel!!.projectsState.allItems!!
                    projectsAvailable = projects.isNotEmpty()
                    if(projectsAvailable) {
                        items(
                            items = projects,
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
            }
        }
        EmptyListUI(
            animations = UIAnimations(
                visible = !projectsAvailable,
                onEnter = fadeIn(),
                onExit = fadeOut()
            ),
            icon = Icons.Default.FolderOff,
            subText = Res.string.no_projects_available,
            textStyle = TextStyle(
                fontFamily = bodyFontFamily
            ),
            themeColor = MaterialTheme.colorScheme.inversePrimary
        )
    }

    @Composable
    @NonRestartableComposable
    private fun SectionHeader(
        header: StringResource
    ) {
        val filter = remember { mutableStateOf(false) }
        val isAllProjectsFiltering = header == Res.string.all
        val areFiltersSet = viewModel!!.areFiltersSet(
            allProjects = isAllProjectsFiltering
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(header)
            )
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {
                    if(areFiltersSet) {
                        viewModel!!.clearFilter(
                            allProjects = isAllProjectsFiltering
                        )
                    } else
                        filter.value = true
                }
            ) {
                Icon(
                    imageVector = if(areFiltersSet)
                        Icons.Default.FilterListOff
                    else
                        Icons.Default.FilterList,
                    contentDescription = null
                )
            }
        }
        FilterProjects(
            show = filter,
            viewModel = viewModel!!,
            isAllProjectsFiltering = isAllProjectsFiltering
        )
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