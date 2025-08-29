package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.pandoro.ui.icons.MoveChangeNote
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.Update
import com.tecknobit.pandoro.ui.theme.AppTypography
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.move_change_note
import pandoro.composeapp.generated.resources.move_change_note_hint

// TODO: TO COMMENT
@Composable
fun MoveChangeNoteDialog(
    show: MutableState<Boolean>,
    viewModel: ProjectScreenViewModel,
    changeNote: Note,
    sourceUpdate: Update,
    availableDestinationUpdates: List<Update>
) {
    val destinationUpdate = remember { mutableStateOf(availableDestinationUpdates.firstOrNull()) }
    EquinoxAlertDialog(
        icon = MoveChangeNote,
        show = show,
        title = Res.string.move_change_note,
        text = {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MoveChangeNoteHint()
                AvailableDestinationUpdates(
                    availableDestinationUpdates = availableDestinationUpdates,
                    destinationUpdate = destinationUpdate
                )
            }
        },
        confirmAction = {
            viewModel.moveChangeNote(
                changeNote = changeNote,
                sourceUpdate = sourceUpdate,
                destinationUpdate = destinationUpdate.value!!,
                onMove = { show.value = false }
            )
        }
    )
}

@Composable
private fun MoveChangeNoteHint() {
    Text(
        text = stringResource(Res.string.move_change_note_hint),
        style = AppTypography.labelLarge
    )
}

@Composable
private fun AvailableDestinationUpdates(
    availableDestinationUpdates: List<Update>,
    destinationUpdate: MutableState<Update?>
) {
    LazyColumn (
        modifier = Modifier
            .selectableGroup()
    ) {
        items(
            items = availableDestinationUpdates,
            key = { update -> update.id }
        ) { update ->
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = destinationUpdate.value == update,
                    onClick = { destinationUpdate.value = update }
                )
                Text(
                    text = update.targetVersion.asVersionText()
                )
            }
        }
    }
}