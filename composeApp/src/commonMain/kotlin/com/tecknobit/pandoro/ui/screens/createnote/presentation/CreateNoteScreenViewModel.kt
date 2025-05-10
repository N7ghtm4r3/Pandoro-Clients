package com.tecknobit.pandoro.ui.screens.createnote.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [CreateNoteScreenViewModel] provides the methods for the creation or the editing of a
 * [com.tecknobit.pandoro.ui.screens.notes.data.Note] item
 *
 * @param projectId The identifier of the project owns the update
 * @param updateId The identifier of the update owns the note
 * @param noteId The identifier of the note to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 */
class CreateNoteScreenViewModel(
    private val projectId: String?,
    private val updateId: String?,
    private val noteId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_note` -> state flow holds the note data
     */
    private val _note = MutableStateFlow<Note?>(
        value = null
    )
    val note: StateFlow<Note?> = _note

    /**
     * `content` -> state holds the content of the note
     */
    lateinit var content: MutableState<String>

    /**
     * Method to retrieve the data of a [Note]
     */
    fun retrieveNote() {
        noteId?.let {
            viewModelScope.launch {
                requester.sendRequest(
                    request = {
                        getNote(
                            noteId = noteId
                        )
                    },
                    onSuccess = {
                        _note.value = Json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = { showSnackbarMessage(it) }
                )
            }
        }
    }

    /**
     * Method to save the [content] inserted
     */
    fun saveNote() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    workOnNote(
                        noteId = noteId,
                        projectId = projectId,
                        updateId = updateId,
                        contentNote = content.value
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}