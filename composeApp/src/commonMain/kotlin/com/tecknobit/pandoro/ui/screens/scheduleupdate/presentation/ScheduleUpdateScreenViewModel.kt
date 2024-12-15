package com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isContentNoteValid

class ScheduleUpdateScreenViewModel(
    projectId: String
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

}