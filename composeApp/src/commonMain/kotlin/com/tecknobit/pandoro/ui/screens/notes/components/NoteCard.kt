package com.tecknobit.pandoro.ui.screens.notes.components

import CircleDashedCheck
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.pandoro.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.copyToClipboard
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsDateString
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteNote
import com.tecknobit.pandoro.ui.icons.Copy
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import com.tecknobit.pandoro.ui.theme.Green
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.note_completed_on
import pandoro.composeapp.generated.resources.note_content_copied
import pandoro.composeapp.generated.resources.note_created_on

@Composable
@NonRestartableComposable
fun NoteCard(
    modifier: Modifier,
    viewModel: NotesScreenViewModel,
    note: Note
) {
    Card(
        modifier = modifier
            .then(
                if (note.markedAsDone) {
                    Modifier.colorOneSideBorder(
                        borderToColor = BorderToColor.END,
                        color = Green(),
                        width = 8.dp,
                        shape = CardDefaults.shape
                    )
                } else
                    Modifier
            ),
        onClick = { navigator.navigate("$CREATE_NOTE_SCREEN/${note.id}") }
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = 16.dp
                ),
            text = if(note.markedAsDone)
                stringResource(Res.string.note_completed_on, note.markAsDoneDate.formatAsDateString())
            else
                stringResource(Res.string.note_created_on, note.creationDate.formatAsDateString()),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
            text = note.content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        NoteActions(
            viewModel = viewModel,
            note = note
        )
    }
}

@Composable
@NonRestartableComposable
private fun NoteActions(
    viewModel: NotesScreenViewModel,
    note: Note
) {
    HorizontalDivider()
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(
            onClick = {
                viewModel.manageNoteStatus(
                    note = note
                )
            }
        ) {
            Icon(
                imageVector = if(note.markedAsDone)
                    CircleDashedCheck
                else
                    Icons.Default.CheckCircle,
                contentDescription = null,
            )
        }
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            val noteCopiedMessage = stringResource(Res.string.note_content_copied)
            IconButton(
                onClick = {
                    copyToClipboard(
                        content = note.content,
                        onCopy = { viewModel.showSnackbarMessage(noteCopiedMessage) }
                    )
                }
            ) {
                Icon(
                    imageVector = Copy,
                    contentDescription = null,
                )
            }
            val deleteNote = remember { mutableStateOf(false) }
            IconButton(
                onClick = { deleteNote.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            DeleteNote(
                viewModel = viewModel,
                show = deleteNote,
                note = note,
                onDelete = { viewModel.notesState.refresh() }
            )
        }
    }
}