package com.tecknobit.pandoro.ui.screens.project.presentation

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [ProjectScreenViewModel] provides the methods to display and operate on a
 * [com.tecknobit.pandoro.ui.screens.projects.data.Project] item
 *
 * @param projectId The identifier of the group to display
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see BaseProjectViewModel
 * @see ProjectDeleter
 * @see NotesManager
 */
class ProjectScreenViewModel(
    private val projectId: String
) : BaseProjectViewModel(), ProjectDeleter, NotesManager {

    /**
     * **updateStatusesFilters** -> the statuses of The update to use as filters
     */
    lateinit var updateStatusesFilters: SnapshotStateList<UpdateStatus>

    /**
     * Method to retrieve the data of a [Project]
     */
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

    /**
     * Method to manage the values in the [updateStatusesFilters] list
     *
     * @param updateStatus The status to remove or add to the list
     * @param selected Whether add or remove the [updateStatus]
     */
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

    /**
     * Method to check whether the [updateStatusesFilters] are set
     *
     * @return whether the [updateStatusesFilters] are set as [Boolean]
     */
    fun areFiltersSet() : Boolean {
        return updateStatusesFilters.size != UpdateStatus.entries.size
    }

    /**
     * Method to clear the filters and set it as the default
     */
    fun clearFilters() {
        updateStatusesFilters.clear()
        updateStatusesFilters.addAll(UpdateStatus.entries)
    }

    /**
     * Method to arrange The updates list applying the filters
     * 
     * @return The updates list filtered as [List] of [ProjectUpdate]
     */
    fun arrangeUpdatesList() : List<ProjectUpdate> {
        return _project.value!!.updates
            .filter { update -> updateStatusesFilters.contains(update.status) }
    }

    /**
     * Method to start an update, so set its status on [UpdateStatus.IN_DEVELOPMENT]
     * 
     * @param update The update to start
     */
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

    /**
     * Method to manage the status of the [note]
     *
     * @param update The update owner of the [note]
     * @param note The note to manage
     */
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

    /**
     * Method to delete a [note]
     *
     * @param update The update owner of the [note]
     * @param note The note to delete
     * @param onDelete The action to execute when the note has been deleted
     */
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

    /**
     * Method to publish an update, so set its status on [UpdateStatus.PUBLISHED]
     *
     * @param update The update to start
     */
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

    /**
     * Method to delete an update
     *
     * @param update The update to delete
     * @param onDelete The action to execute when the update has been deleted
     */
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