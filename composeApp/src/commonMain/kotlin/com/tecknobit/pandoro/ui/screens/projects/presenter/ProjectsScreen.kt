@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.projects.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.components.InDevelopmentProjectCard
import com.tecknobit.pandoro.ui.components.ProjectCard
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalGrid
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.in_development
import pandoro.composeapp.generated.resources.projects

class ProjectsScreen: PandoroScreen<ProjectsScreenViewModel>(
    viewModel = ProjectsScreenViewModel()
) {

    private lateinit var inDevelopmentProjects: State<List<InDevelopmentProject>>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
            bottomBar = { AdaptBottomBarToNavigationMode() }
        ) {
            AdaptContentToNavigationMode {
                Text(
                    text = stringResource(Res.string.projects),
                    fontFamily = displayFontFamily,
                    fontSize = 35.sp
                )
                ProjectsInDevelopmentSection()
                ProjectsSection()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsInDevelopmentSection() {
        Column {
            Text(
                text = stringResource(Res.string.in_development)
            )
            LazyRow (
                contentPadding = PaddingValues(
                    vertical = 10.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = inDevelopmentProjects.value,
                    key = { inDevelopmentProject -> inDevelopmentProject.update.id }
                ) { inDevelopmentProject ->
                    InDevelopmentProjectCard(
                        inDevelopmentProject = inDevelopmentProject
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsSection() {
        Column {
            Text(
                text = stringResource(Res.string.all)
            )
            ProjectsList()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsList() {
        val windowWidthSizeClass = getCurrentWidthSizeClass()
        when(windowWidthSizeClass) {
            Expanded, Medium -> {
                PaginatedLazyVerticalGrid(
                    paginationState = viewModel!!.projectsState,
                    columns = GridCells.Adaptive(
                        minSize = 250.dp
                    ),
                    contentPadding = PaddingValues(
                        vertical = 10.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = viewModel!!.projectsState.allItems!!,
                        key = { project -> project.id }
                    ) { project ->
                        ProjectCard(
                            modifier = Modifier
                                .size(
                                    width = 250.dp,
                                    height = 150.dp
                                ),
                            project = project
                        )
                    }
                }
            } else -> {
                PaginatedLazyColumn(
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
                    items(
                        items = viewModel!!.projectsState.allItems!!,
                        key = { project -> project.id }
                    ) { project ->
                        ProjectCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(
                                    height = 175.dp
                                ),
                            project = project
                        )
                    }
                }
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        inDevelopmentProjects = viewModel!!.inDevelopmentProject.collectAsState()
    }

}