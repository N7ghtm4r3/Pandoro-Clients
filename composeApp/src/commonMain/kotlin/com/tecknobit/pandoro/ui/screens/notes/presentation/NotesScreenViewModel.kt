package com.tecknobit.pandoro.ui.screens.notes.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import io.github.ahmad_hamwi.compose.pagination.PaginationState

class NotesScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), NotesManager {

    lateinit var selectToDoNotes: MutableState<Boolean>

    lateinit var selectCompletedNotes: MutableState<Boolean>

    val notesState = PaginationState<Int, Note>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveNotes(
                page = page
            )
        }
    )

    private fun retrieveNotes(
        page: Int
    ) {
        requester.sendPaginatedWRequest(
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

    fun manageToDoNotesFilter() {
        selectToDoNotes.value = !selectToDoNotes.value
        notesState.refresh()
    }

    fun manageCompletedNotesFilter() {
        selectCompletedNotes.value = !selectCompletedNotes.value
        notesState.refresh()
    }

    override fun manageNoteStatus(
        update: ProjectUpdate?,
        note: Note
    ) {
        requester.sendWRequest(
            request = {
                changeNoteStatus(
                    noteId = note.id,
                    completed = !note.markedAsDone
                )
            },
            onSuccess = {
                notesState.refresh()
            },
            onFailure = { showSnackbarMessage(it.toResponseContent())}
        )
    }

    override fun deleteNote(
        update: ProjectUpdate?,
        note: Note,
        onDelete: () -> Unit
    ) {
        requester.sendWRequest(
            request = {
                deleteNote(
                    noteId = note.id
                )
            },
            onSuccess = {
                onDelete.invoke()
            },
            onFailure = { showSnackbarMessage(it.toResponseContent())}
        )
    }

}