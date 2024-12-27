package com.tecknobit.pandoro.ui.screens.createnote.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class CreateNoteScreenViewModel(
    private val projectId: String?,
    private val updateId: String?,
    private val noteId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _note = MutableStateFlow<Note?>(
        value = null
    )
    val note: StateFlow<Note?> = _note

    lateinit var content: MutableState<String>

    fun retrieveNote() {
        noteId?.let {
            requester.sendWRequest(
                request = {
                    getNote(
                        noteId = noteId
                    )
                },
                onSuccess = {
                    _note.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = {
                    showSnackbarMessage(it.toResponseContent())
                }
            )
        }
    }

    fun saveNote() {
        requester.sendWRequest(
            request = {
                workOnNote(
                    noteId = noteId,
                    projectId = projectId,
                    updateId = updateId,
                    contentNote = content.value
                )
            },
            onSuccess = {
                navigator.goBack()
            },
            onFailure = {
                 showSnackbarMessage(it.toResponseContent())
            }
        )
    }

}