package com.tecknobit.pandoro.ui.screens.project.presentation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ProjectScreenViewModel(
    private val projectId: String
) : BaseProjectViewModel(), ProjectDeleter, NotesManager {

    lateinit var updateStatusesFilters: SnapshotStateList<UpdateStatus>

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
        arrangeUpdatesList()
    }

    fun areFiltersSet() : Boolean {
        return updateStatusesFilters.size != UpdateStatus.entries.size
    }

    fun clearFilters() {
        updateStatusesFilters.clear()
        updateStatusesFilters.addAll(UpdateStatus.entries)
    }

    fun arrangeUpdatesList() : List<ProjectUpdate> {
        return _project.value!!.updates
            .filter { update -> updateStatusesFilters.contains(update.status) }
    }

    fun startUpdate(
        update: ProjectUpdate
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    startUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = {},
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    override fun manageNoteStatus(
        update: ProjectUpdate?,
        note: Note
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    workOnChangeNoteStatus(
                        projectId = projectId,
                        updateId = update!!.id,
                        changeNoteId = note.id,
                        completed = !note.markedAsDone
                    )
                },
                onSuccess = {},
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    override fun deleteNote(
        update: ProjectUpdate?,
        note: Note,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    deleteChangeNote(
                        projectId = projectId,
                        updateId = update!!.id,
                        changeNoteId = note.id,
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    fun publishUpdate(
        update: ProjectUpdate
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    publishUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = { },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    fun deleteUpdate(
        update: ProjectUpdate,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    deleteUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

}