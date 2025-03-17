package com.tecknobit.pandoro.ui.screens.notes.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

/**
 * The **NotesScreenViewModel** class is the support class used to manage the
 * [com.tecknobit.pandoro.ui.screens.notes.presenter.NotesScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel

 * @see Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see NotesManager
 */
class NotesScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), NotesManager {

    /**
     * `selectToDoNotes` -> whether select the to-do notes
     */
    lateinit var selectToDoNotes: MutableState<Boolean>

    /**
     * `selectCompletedNotes` -> whether select the completed notes
     */
    lateinit var selectCompletedNotes: MutableState<Boolean>

    /**
     * `notesState` -> the state used to manage the pagination for the
     * [retrieveNotes] method
     */
    val notesState = PaginationState<Int, Note>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveNotes(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the notes owned by the [com.tecknobit.pandoro.localUser]
     *
     * @param page The page to request to the server
     */
    private fun retrieveNotes(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                serializer = Note.serializer(),
                request = {
                    getNotes(
                        page = page,
                        selectToDoNotes = selectToDoNotes.value,
                        selectCompletedNotes = selectCompletedNotes.value
                    )
                },
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    notesState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                    notesState.setError(Exception())
                }
            )
        }
    }

    /**
     * Method to manage the [selectToDoNotes] filter and apply if needed to the [notesState]
     */
    fun manageToDoNotesFilter() {
        selectToDoNotes.value = !selectToDoNotes.value
        notesState.refresh()
    }

    /**
     * Method to manage the [selectCompletedNotes] filter and apply if needed to the [notesState]
     */
    fun manageCompletedNotesFilter() {
        selectCompletedNotes.value = !selectCompletedNotes.value
        notesState.refresh()
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
            requester.sendRequest(
                request = {
                    changeNoteStatus(
                        noteId = note.id,
                        completed = !note.markedAsDone
                    )
                },
                onSuccess = {
                    notesState.refresh()
                },
                onFailure = { showSnackbarMessage(it)}
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
            requester.sendRequest(
                request = {
                    deleteNote(
                        noteId = note.id
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = { showSnackbarMessage(it)}
            )
        }
    }

}