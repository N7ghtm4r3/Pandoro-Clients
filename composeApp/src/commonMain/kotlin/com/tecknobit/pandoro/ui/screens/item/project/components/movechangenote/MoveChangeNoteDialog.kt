package com.tecknobit.pandoro.ui.screens.item.project.components.movechangenote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.pandoro.ui.icons.MoveChangeNote
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.item.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.data.project.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.shared.data.project.Update
import com.tecknobit.pandoro.ui.theme.AppTypography
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.move_change_note
import pandoro.composeapp.generated.resources.move_change_note_hint

/**
 * Dialog used to allow the user to select the update where move the change note
 *
 * @param show Whether to show the dialog
 * @param viewModel The support viewmodel of the screen
 * @param changeNote The change note to move
 * @param sourceUpdate The source update where the change note is currently attached
 * @param availableDestinationUpdates The available update where move the change note
 *
 * @since 1.2.0
 */
@Composable
fun MoveChangeNoteDialog(
    show: MutableState<Boolean>,
    viewModel: ProjectScreenViewModel,
    changeNote: Note,
    sourceUpdate: Update,
    availableDestinationUpdates: List<Update>
) {
    val destinationUpdate = remember { mutableStateOf(availableDestinationUpdates.firstOrNull()) }
    LaunchedEffect(destinationUpdate.value) {
        println(destinationUpdate.value?.targetVersion)
    }
    EquinoxAlertDialog(
        icon = MoveChangeNote,
        show = show,
        title = Res.string.move_change_note,
        text = {
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MoveChangeNoteHint()
                DestinationUpdateSelector(
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

/**
 * Hint text about the update selection
 */
@Composable
private fun MoveChangeNoteHint() {
    Text(
        text = stringResource(Res.string.move_change_note_hint),
        style = AppTypography.labelLarge
    )
}

/**
 * Selector used to select the destination update where move the change note
 *
 * @param availableDestinationUpdates The available update where move the change note
 * @param destinationUpdate The current update selected as destination of the change note
 *
 * @since 1.2.0
 */
@Composable
private fun DestinationUpdateSelector(
    availableDestinationUpdates: List<Update>,
    destinationUpdate: MutableState<Update?>
) {
    LazyColumn (
        modifier = Modifier
            .selectableGroup()
            .heightIn(
                max = 300.dp
            )
    ) {
        items(
            items = availableDestinationUpdates,
            key = { update -> update.id }
        ) { update ->
            val isTheSelectedUpdate = destinationUpdate.value!!.id == update.id
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isTheSelectedUpdate,
                    onClick = { destinationUpdate.value = update }
                )
                Text(
                    modifier = Modifier
                        .selectable(
                            selected = isTheSelectedUpdate,
                            role = Role.RadioButton,
                            onClick = { destinationUpdate.value = update }
                        ),
                    text = update.targetVersion.asVersionText()
                )
            }
        }
    }
}