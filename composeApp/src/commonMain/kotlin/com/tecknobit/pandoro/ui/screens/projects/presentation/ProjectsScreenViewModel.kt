package com.tecknobit.pandoro.ui.screens.projects.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * The [ProjectsScreenViewModel] is useful to manage the lists displayed in the
 * [com.tecknobit.pandoro.ui.screens.projects.presenter.ProjectsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see MultipleListViewModel
 * @see ProjectDeleter
 */
class ProjectsScreenViewModel : MultipleListViewModel(), ProjectDeleter {

    /**
     * `requestsScope` coroutine used to send the requests to the backend
     */
    override val requestsScope: CoroutineScope = MainScope()

    /**
     * `inDevelopmentProjectsFilter` the filters to apply to the [inDevelopmentProjectsState] list
     */
    lateinit var inDevelopmentProjectsFilter: MutableState<String>

    /**
     * `inDevelopmentProjectsState` the state used to manage the pagination for the
     * [retrieveInDevelopmentProjects] method
     */
    val inDevelopmentProjectsState = PaginationState<Int, InDevelopmentProject>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveInDevelopmentProjects(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the projects currently
     * [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT]
     *
     * @param page The page to request to the server
     */
    private fun retrieveInDevelopmentProjects(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getInDevelopmentProjects(
                        page = page,
                        filters = inDevelopmentProjectsFilter.value
                    )
                },
                serializer = Project.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    inDevelopmentProjectsState.appendPage(
                        items = paginatedResponse.data.toDevelopmentProjects(),
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                    inDevelopmentProjectsState.setError(Exception())
                }
            )
        }
    }

    /**
     * Method to transform a list of [Project] in a list of [InDevelopmentProject]
     *
     * @return the list transformed as [List] of [InDevelopmentProject]
     */
    private fun List<Project>.toDevelopmentProjects() : List<InDevelopmentProject> {
        val inDevelopmentProjects = arrayListOf<InDevelopmentProject>()
        this.forEach { project: Project ->
            project.updates.forEach { update ->
                inDevelopmentProjects.add(
                    InDevelopmentProject(
                        project = project,
                        update = update
                    )
                )
            }
        }
        inDevelopmentProjects.sortByDescending { inDevelopmentProject ->
            inDevelopmentProject.update.startDate
        }
        return inDevelopmentProjects
    }

    /**
     * `projectsFilter` the filters to apply to the [projectsState] list
     */
    lateinit var projectsFilter: MutableState<String>

    /**
     * `projectsState` the state used to manage the pagination for the
     * [retrieveProjects] method
     */
    val projectsState = PaginationState<Int, Project>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveProjects(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the complete projects list of the [com.tecknobit.pandoro.localUser]
     *
     * @param page The page to request to the server
     */
    private fun retrieveProjects(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getProjects(
                        page = page,
                        filters = projectsFilter.value
                    )
                },
                serializer = Project.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    projectsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                    projectsState.setError(Exception())
                }
            )
        }
    }

    /**
     * Method to retry the retrieving of the lists data
     */
    override fun retryRetrieveLists() {
        inDevelopmentProjectsState.retryLastFailedRequest()
        projectsState.retryLastFailedRequest()
    }

    /**
     * Method to check whether the filters have been set
     *
     * @param allItems Whether check the all items list
     */
    override fun areFiltersSet(
        allItems: Boolean
    ): Boolean {
        return if(allItems)
            projectsFilter.value.isNotEmpty()
        else
            inDevelopmentProjectsFilter.value.isNotEmpty()
    }

    /**
     * Method to clear filters have been set
     *
     * @param allItems Whether clear the all items filters
     */
    override fun clearFilters(
        allItems: Boolean
    ) {
        if(allItems) {
            projectsFilter.value = ""
            projectsState.refresh()
        } else {
            inDevelopmentProjectsFilter.value = ""
            inDevelopmentProjectsState.refresh()
        }
    }

    /**
     * Method to filter the items list
     *
     * @param allItems Whether filter the all items list
     * @param filters The filters to use
     * @param onFiltersSet The action to execute when the filters have been set
     */
    override fun filterItems(
        allItems: Boolean,
        filters: MutableState<String>,
        onFiltersSet: () -> Unit
    ) {
        if(allItems) {
            projectsFilter.value = filters.value
            projectsState.refresh()
        } else {
            inDevelopmentProjectsFilter.value = filters.value
            inDevelopmentProjectsState.refresh()
        }
        onFiltersSet.invoke()
    }

}