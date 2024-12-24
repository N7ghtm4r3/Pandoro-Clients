@file:OptIn(ExperimentalPaginationApi::class)

package com.tecknobit.pandoro.ui.screens.project.presentation

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE_SIZE
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import com.tecknobit.pandorocore.enums.UpdateStatus
import io.github.ahmad_hamwi.compose.pagination.ExperimentalPaginationApi
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ProjectScreenViewModel(
    private val projectId: String
) : BaseProjectViewModel(), ProjectDeleter, NotesManager {

    private val totalUpdates = mutableSetOf<ProjectUpdate>()

    private val currentUpdatesLoaded = mutableListOf<ProjectUpdate>()

    lateinit var updateStatusesFilters: SnapshotStateList<UpdateStatus>

    val updatesState = PaginationState<Int, ProjectUpdate>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            appendUpdates(
                page = page
            )
        }
    )

    override fun retrieveProject() {
        execRefreshingRoutine(
            currentContext = ProjectScreen::class.java,
            routine = {
                requester.sendWRequest(
                    request = {
                        getProject(
                            projectId = projectId
                        )
                    },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _project.value = Json.decodeFromJsonElement(it.toResponseData())
                        totalUpdates.addAll(_project.value!!.updates)
                    },
                    onFailure = { setHasBeenDisconnectedValue(true) },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            }
        )
    }

    fun manageStatusesFilter(
        updateStatus: UpdateStatus,
        selected: Boolean
    ) {
        if(selected)
            updateStatusesFilters.add(updateStatus)
        else
            updateStatusesFilters.remove(updateStatus)
        filterList()
    }

    fun areFiltersSet() : Boolean {
        return updateStatusesFilters.size != UpdateStatus.entries.size
    }

    fun clearFilters() {
        updateStatusesFilters.clear()
        updateStatusesFilters.addAll(UpdateStatus.entries)
        filterList()
    }

    private fun filterList() {
        updatesState.appendPageWithUpdates(
            allItems = totalUpdates.filter { update -> updateStatusesFilters.contains(update.status) },
            nextPageKey = 0,
            isLastPage = true
        )
    }

    private fun appendUpdates(
        page: Int
    ) {
        val toIndex = ((page + 1) * DEFAULT_PAGE_SIZE)
        val lastIndex = totalUpdates.size
        val pagedUpdates = totalUpdates.toList().subList(
            fromIndex = currentUpdatesLoaded.size,
            toIndex = if(toIndex > lastIndex)
                lastIndex
            else
                toIndex
        )
        currentUpdatesLoaded.addAll(pagedUpdates)
        updatesState.appendPage(
            items = pagedUpdates,
            nextPageKey = page + 1,
            isLastPage = currentUpdatesLoaded.size == totalUpdates.size
        )
    }

    fun startUpdate(
        update: ProjectUpdate
    ) {
        requester.sendWRequest(
            request = {
                startUpdate(
                    projectId = projectId,
                    updateId = update.id
                )
            },
            onSuccess = {
                // TODO: TO FIX THE ISSUE ABOUT THE REFRESHING 
                updatesState.refresh()
            },
            onFailure = { showSnackbarMessage(it.toResponseContent()) }
        )
    }

    override fun manageNoteStatus(
        update: ProjectUpdate?,
        note: Note
    ) {
        TODO("Not yet implemented")
    }

    override fun deleteNote(
        update: ProjectUpdate?,
        note: Note,
        onDelete: () -> Unit
    ) {
        TODO("Not yet implemented")
    }

    fun publishUpdate(
        update: ProjectUpdate
    ) {
        requester.sendWRequest(
            request = {
                publishUpdate(
                    projectId = projectId,
                    updateId = update.id
                )
            },
            onSuccess = { updatesState.refresh() },
            onFailure = { showSnackbarMessage(it.toResponseContent()) }
        )
    }

    fun deleteUpdate(
        update: ProjectUpdate,
        onDelete: () -> Unit
    ) {
        requester.sendWRequest(
            request = {
                deleteUpdate(
                    projectId = projectId,
                    updateId = update.id
                )
            },
            onSuccess = {
                updatesState.refresh()
                onDelete.invoke()
            },
            onFailure = { showSnackbarMessage(it.toResponseContent()) }
        )
    }

}