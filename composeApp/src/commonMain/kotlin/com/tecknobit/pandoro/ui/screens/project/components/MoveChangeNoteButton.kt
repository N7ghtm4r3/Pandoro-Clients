package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tecknobit.pandoro.ui.icons.MoveChangeNote
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Update
import com.tecknobit.pandorocore.enums.UpdateStatus
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.move_change_note

// TODO: TO COMMENT 1.2.0
@Composable
fun MoveChangeNoteButton(
    viewModel: ProjectScreenViewModel,
    changeNote: Note,
    update: Update,
) {
    val moveChangeNote = remember { mutableStateOf(false) }
    val availableDestinationUpdates = viewModel.retrieveAvailableDestinationUpdates(
        sourceUpdate = update
    )
    IconButton(
        onClick = { moveChangeNote.value = !moveChangeNote.value },
        enabled = update.status != UpdateStatus.PUBLISHED && !changeNote.markedAsDone &&
                availableDestinationUpdates.isNotEmpty()
    ) {
        Icon(
            imageVector = MoveChangeNote,
            contentDescription = stringResource(Res.string.move_change_note)
        )
    }
    MoveChangeNoteDialog(
        show = moveChangeNote,
        viewModel = viewModel,
        changeNote = changeNote,
        sourceUpdate = update,
        availableDestinationUpdates = availableDestinationUpdates
    )
}