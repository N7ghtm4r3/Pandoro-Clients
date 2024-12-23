package com.tecknobit.pandoro.ui.screens.projects.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

class ProjectsScreenViewModel : MultipleListViewModel(), ProjectDeleter {

    lateinit var inDevelopmentProjectsFilter: MutableState<String>

    val inDevelopmentProjectsState = PaginationState<Int, InDevelopmentProject>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveInDevelopmentProjects(
                page = page
            )
        }
    )

    private fun retrieveInDevelopmentProjects(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
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

    lateinit var projectsFilter: MutableState<String>

    val projectsState = PaginationState<Int, Project>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveProjects(
                page = page
            )
        }
    )

    private fun retrieveProjects(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
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

    override fun retryRetrieveLists() {
        inDevelopmentProjectsState.retryLastFailedRequest()
        projectsState.retryLastFailedRequest()
    }

    override fun areFiltersSet(
        allItems: Boolean
    ): Boolean {
        return if(allItems)
            projectsFilter.value.isNotEmpty()
        else
            inDevelopmentProjectsFilter.value.isNotEmpty()
    }

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