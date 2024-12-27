package com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isContentNoteValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.wrong_change_notes_list

class ScheduleUpdateScreenViewModel(
    private val projectId: String
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var targetVersion: MutableState<String>

    lateinit var targetVersionError: MutableState<Boolean>

    lateinit var changeNoteContent: MutableState<String>

    lateinit var changeNoteContentError: MutableState<Boolean>

    val changeNotes: SnapshotStateList<String> = mutableStateListOf()

    fun addChangeNote() {
        if(!isContentNoteValid(changeNoteContent.value)) {
            changeNoteContentError.value = true
            return
        }
        changeNotes.add(changeNoteContent.value)
        changeNoteContent.value = ""
    }

    fun removeChangeNote(
        changeNote: String
    ) {
        changeNotes.remove(changeNote)
    }

    fun scheduleUpdate() {
        if(!isValidVersion(targetVersion.value)) {
            targetVersionError.value = true
            return
        }
        if(changeNotes.isEmpty()) {
            viewModelScope.launch {
                showSnackbarMessage(
                    message = getString(
                        resource = Res.string.wrong_change_notes_list
                    )
                )
            }
            return
        }
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    scheduleUpdate(
                        projectId = projectId,
                        targetVersion = targetVersion.value,
                        updateChangeNotes = changeNotes
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

}