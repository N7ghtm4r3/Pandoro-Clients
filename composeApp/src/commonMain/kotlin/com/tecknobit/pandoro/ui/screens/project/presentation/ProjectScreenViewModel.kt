@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.project.presentation

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.util.toggle
import com.tecknobit.pandoro.helpers.KReviewer
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Update
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel.ProjectDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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
 *
 * @see EquinoxViewModel
 * @see BaseProjectViewModel
 * @see ProjectDeleter
 * @see NotesManager
 */
class ProjectScreenViewModel(
    private val projectId: String
) : BaseProjectViewModel(), ProjectDeleter, NotesManager {

    /**
     * `requestsScope` coroutine used to send the requests to the backend
     */
    override val requestsScope: CoroutineScope = MainScope()

    /**
     * `updateStatusesFilters` the statuses of The update to use as filters
     */
    lateinit var updateStatusesFilters: SnapshotStateList<UpdateStatus>

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    lateinit var sessionFlowState: SessionFlowState

    /**
     * Method to retrieve the data of a [Project]
     */
    override fun retrieveProject() {
        retrieve(
            currentContext = ProjectScreen::class,
            routine = {
                requester.sendRequest(
                    request = {
                        getProject(
                            projectId = projectId
                        )
                    },
                    onSuccess = {
                        sessionFlowState.notifyOperational()
                        _project.value = Json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = {},
                    onConnectionError = { sessionFlowState.notifyServerOffline() }
                )
            }
        )
    }

    /**
     * Method to manage the values in the [updateStatusesFilters] list
     *
     * @param updateStatus The status to remove or add to the list
     */
    fun manageStatusesFilter(
        updateStatus: UpdateStatus
    ) {
        updateStatusesFilters.toggle(
            element = updateStatus
        )
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
     * Method to arrange the updates list applying the filters
     * 
     * @return The updates list filtered as [List] of [Update]
     */
    fun arrangeUpdatesList() : SnapshotStateList<Update> {
        return _project.value!!.updates
            .filter { update -> updateStatusesFilters.contains(update.status) }
            .toMutableStateList()
    }

    /**
     * Method to start an update, so set its status on [UpdateStatus.IN_DEVELOPMENT]
     * 
     * @param update The update to start
     */
    fun startUpdate(
        update: Update
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    startUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = {},
                onFailure = { showSnackbarMessage(it) }
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
        update: Update?,
        note: Note
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    workOnChangeNoteStatus(
                        projectId = projectId,
                        updateId = update!!.id,
                        changeNoteId = note.id,
                        completed = !note.markedAsDone
                    )
                },
                onSuccess = {},
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    // TODO: TO COMMENT 1.2.0
    fun retrieveAvailableDestinationUpdates(
        sourceUpdate: Update
    ): List<Update> {
        return _project.value!!.updates.filter { update ->
            update.status != UpdateStatus.PUBLISHED && sourceUpdate.id != update.id
        }
    }

    fun moveChangeNote(
        changeNote: Note,
        sourceUpdate: Update,
        destinationUpdate: Update,
        onMove: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onMove()
    }

    /**
     * Method to delete a [note]
     *
     * @param update The update owner of the [note]
     * @param note The note to delete
     * @param onDelete The action to execute when the note has been deleted
     */
    override fun deleteNote(
        update: Update?,
        note: Note,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteChangeNote(
                        projectId = projectId,
                        updateId = update!!.id,
                        changeNoteId = note.id,
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to publish an update, so set its status on [UpdateStatus.PUBLISHED]
     *
     * @param update The update to start
     */
    fun publishUpdate(
        update: Update
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    publishUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp()
                },
                onFailure = { showSnackbarMessage(it) }
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
        update: Update,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteUpdate(
                        projectId = projectId,
                        updateId = update.id
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}