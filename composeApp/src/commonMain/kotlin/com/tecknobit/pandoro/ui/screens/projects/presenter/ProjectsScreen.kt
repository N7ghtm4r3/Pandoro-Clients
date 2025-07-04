@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.projects.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.projects.components.FilterProjects
import com.tecknobit.pandoro.ui.screens.projects.components.InDevelopmentProjectCard
import com.tecknobit.pandoro.ui.screens.projects.components.Projects
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.screens.ListsScreen
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.empty_filtered_projects
import pandoro.composeapp.generated.resources.in_development
import pandoro.composeapp.generated.resources.projects

/**
 * The [ProjectsScreen] displays the projects lists of the [com.tecknobit.pandoro.localUser] such
 * projects in development and the complete projects list
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see ListsScreen
 */
class ProjectsScreen: ListsScreen<ProjectsScreenViewModel>(
    viewModel = ProjectsScreenViewModel(),
    screenTitle = Res.string.projects
) {

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
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

    /**
     * The horizontal list to display the items in row format, the projects currently
     * [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT]
     */
    @Composable
    override fun ItemsInRow() {
        var projectsAvailable by remember { mutableStateOf(false) }
        if(projectsAvailable || viewModel.inDevelopmentProjectsFilter.value.isNotEmpty()) {
            SectionHeader(
                header = Res.string.in_development,
                isAllItemsFiltering = false
            )
        }
        PaginatedLazyRow(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel.inDevelopmentProjectsState,
            contentPadding = PaddingValues(
                vertical = if(projectsAvailable)
                    10.dp
                else
                    0.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            firstPageEmptyIndicator = {
                projectsAvailable = false
                if(viewModel.inDevelopmentProjectsFilter.value.isNotEmpty()) {
                    EmptyResultWithFilters(
                        info = Res.string.empty_filtered_projects
                    )
                }
            },
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
        ) {
            items(
                items = viewModel.inDevelopmentProjectsState.allItems!!,
                key = { inDevelopmentProject -> inDevelopmentProject.update.id }
            ) { inDevelopmentProject ->
                projectsAvailable = true
                InDevelopmentProjectCard(
                    inDevelopmentProject = inDevelopmentProject
                )
            }
        }
    }

    /**
     * The column or grid list dynamically adapted based on the screen size, the complete projects
     * list
     */
    @Composable
    @NonRestartableComposable
    override fun ItemsAdaptedSize() {
        SectionHeader(
            header = Res.string.all,
            isAllItemsFiltering = true
        )
        Projects(
            viewModel = viewModel
        )
    }

    /**
     * UI to filter the [ItemsInRow] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    @NonRestartableComposable
    override fun FilterRowItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterProjects(
            show = show,
            viewModel = viewModel,
            isAllProjectsFiltering = false
        )
    }

    /**
     * UI to filter the [ItemsAdaptedSize] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    @NonRestartableComposable
    override fun FilterAllItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterProjects(
            show = show,
            viewModel = viewModel,
            isAllProjectsFiltering = true
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been created
     */
    override fun onCreate() {
        viewModel.setActiveContext(HomeScreen::class)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel.inDevelopmentProjectsFilter = remember { mutableStateOf("") }
        viewModel.projectsFilter = remember { mutableStateOf("") }
    }

}