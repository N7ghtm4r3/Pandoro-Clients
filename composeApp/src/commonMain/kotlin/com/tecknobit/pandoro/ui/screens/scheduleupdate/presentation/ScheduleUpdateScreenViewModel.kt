package com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.helpers.KReviewer
import com.tecknobit.pandoro.helpers.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isContentNoteValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.wrong_change_notes_list

/**
 * The **ScheduleUpdateScreenViewModel** class is the support class used to schedule a new update for
 * a project
 *
 * @param projectId The identifier of the project where the update must be attached
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel * @see com.tecknobit.equinoxcompose.session.Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 */
class ScheduleUpdateScreenViewModel(
    private val projectId: String
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `targetVersion` the value of the target version
     */
    lateinit var targetVersion: MutableState<String>

    /**
     * `targetVersionError` whether the [targetVersion] field is not valid
     */
    lateinit var targetVersionError: MutableState<Boolean>

    /**
     * `changeNoteContent` the content of the change note
     */
    lateinit var changeNoteContent: MutableState<String>

    /**
     * `changeNoteContentError` whether the [changeNoteContent] field is not valid
     */
    lateinit var changeNoteContentError: MutableState<Boolean>

    /**
     * `changeNotes` the list of the change notes of the update
     */
    val changeNotes: SnapshotStateList<String> = mutableStateListOf()

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    @OptIn(ExperimentalComposeApi::class)
    lateinit var sessionFlowState: SessionFlowState

    /**
     * Method to add a change note to the [changeNotes] list
     */
    fun addChangeNote() {
        if(!isContentNoteValid(changeNoteContent.value)) {
            changeNoteContentError.value = true
            return
        }
        changeNotes.add(changeNoteContent.value)
        changeNoteContent.value = ""
    }

    /**
     * Method to remove a change note from the [changeNotes] list
     *
     * @param changeNote The change note to remove
     */
    fun removeChangeNote(
        changeNote: String
    ) {
        changeNotes.remove(changeNote)
    }

    /**
     * Method to schedule the new update
     */
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
            requester.sendRequest(
                request = {
                    scheduleUpdate(
                        projectId = projectId,
                        targetVersion = targetVersion.value,
                        updateChangeNotes = changeNotes
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.popBackStack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}