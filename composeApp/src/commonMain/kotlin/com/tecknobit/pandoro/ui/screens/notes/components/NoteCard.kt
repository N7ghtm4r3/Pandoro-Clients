@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.notes.components

import CircleDashedCheck
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPointType
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.pandoro.CREATE_CHANGE_NOTE_SCREEN
import com.tecknobit.pandoro.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.copyToClipboard
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsDateString
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsTimeString
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteNote
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.icons.ClockLoader20
import com.tecknobit.pandoro.ui.icons.Copy
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import com.tecknobit.pandoro.ui.theme.ChangeNoteBackground
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.content
import pandoro.composeapp.generated.resources.has_completed_the_note
import pandoro.composeapp.generated.resources.has_created_the_note
import pandoro.composeapp.generated.resources.not_available
import pandoro.composeapp.generated.resources.note_completed_in
import pandoro.composeapp.generated.resources.note_completed_on
import pandoro.composeapp.generated.resources.note_content_copied
import pandoro.composeapp.generated.resources.note_created
import pandoro.composeapp.generated.resources.note_created_on
import pandoro.composeapp.generated.resources.timeline
import pandoro.composeapp.generated.resources.yet_to_complete

/**
 * The card to display the note details
 *
 * @param modifier The modifier to apply to component
 * @param viewModel The support viewmodel for the screen
 * @param note The note to display
 */
@Composable
@NonRestartableComposable
fun NoteCard(
    modifier: Modifier,
    viewModel: NotesScreenViewModel,
    note: Note
) {
    NoteCardContent(
        modifier = modifier,
        viewModel = viewModel,
        note = note,
        onDoubleClick = { navigator.navigate("$CREATE_NOTE_SCREEN/${note.id}") },
        onDelete = { viewModel.notesState.refresh() }
    )
}

/**
 * The card to display the change note details
 *
 * @param modifier The modifier to apply to component
 * @param viewModel The support viewmodel for the screen
 * @param project The project owner of the update
 * @param update The update owner of the change note
 * @param note The change note to display
 * @param allowedToChangeStatus Whether the status of the change note can be edited
 */
@Composable
@NonRestartableComposable
fun ChangeNoteCard(
    modifier: Modifier,
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: ProjectUpdate,
    note: Note,
    allowedToChangeStatus: Boolean
) {
    NoteCardContent(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = ChangeNoteBackground()
        ),
        viewModel = viewModel,
        noteShared = project.isSharedWithGroups(),
        update = update,
        allowedToChangeStatus = allowedToChangeStatus,
        allowDeletion = update.status != PUBLISHED,
        note = note,
        onDoubleClick = if(!note.markedAsDone) {
            {
                navigator.navigate(
                    route = "$CREATE_CHANGE_NOTE_SCREEN/${project.id}/${update.id}" +
                            "/${update.targetVersion}/${note.id}"
                )
            }
        } else
            null
    )
}

/**
 * The card container to display the note details
 *
 * @param modifier The modifier to apply to component
 * @param colors The colors to apply to the card
 * @param viewModel The support viewmodel for the screen
 * @param noteShared Whether the note is shared with groups
 * @param allowDeletion Whether the note can be deleted
 * @param update The update owner of the note
 * @param note The note to display
 * @param allowedToChangeStatus Whether the status of the change note can be edited
 * @param onDoubleClick The action to execute when the card is double-clicked
 * @param onDelete The action to execute when the note has been deleted
 */
@Composable
@NonRestartableComposable
private fun NoteCardContent(
    modifier: Modifier,
    colors: CardColors = CardDefaults.cardColors(),
    viewModel: EquinoxViewModel,
    noteShared: Boolean = false,
    allowDeletion: Boolean = true,
    update: ProjectUpdate? = null,
    note: Note,
    allowedToChangeStatus: Boolean = true,
    onDoubleClick: (() -> Unit)?,
    onDelete: () -> Unit = {}
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Card(
        modifier = modifier
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                },
                onDoubleClick = onDoubleClick
            )
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
        colors = colors
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
                )
                .heightIn(
                    min = 75.dp
                ),
            text = note.content,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        NoteActions(
            viewModel = viewModel,
            update = update,
            note = note,
            allowedToChangeStatus = allowedToChangeStatus,
            onDelete = onDelete,
            allowDeletion = allowDeletion
        )
    }
    NoteDetails(
        noteShared = noteShared,
        note = note,
        state = modalBottomSheetState,
        scope = scope
    )
}

/**
 * The actions can be execute on the [note]
 *
 * @param viewModel The support viewmodel for the screen
 * @param update The update owner of the note
 * @param note The note to display
 * @param allowedToChangeStatus Whether the status of the change note can be edited
 * @param onDelete The action to execute when the note has been deleted
 * @param allowDeletion Whether the note can be deleted
 */
@Composable
@NonRestartableComposable
private fun NoteActions(
    viewModel: EquinoxViewModel,
    update: ProjectUpdate?,
    note: Note,
    allowedToChangeStatus: Boolean,
    onDelete: () -> Unit,
    allowDeletion: Boolean
) {
    HorizontalDivider()
    Row (
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(
            enabled = allowedToChangeStatus,
            onClick = {
                (viewModel as NotesManager).manageNoteStatus(
                    update = update,
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
                .fillMaxWidth()
                .padding(
                    end = 5.dp
                ),
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
            AnimatedVisibility(
                visible = allowDeletion
            ) {
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
                    update = update,
                    note = note,
                    onDelete = onDelete
                )
            }
        }
    }
}

/**
 * The layout to display the note details
 *
 * @param noteShared Whether the note is shared with groups
 * @param note The note to display
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 */
@Composable
@NonRestartableComposable
private fun NoteDetails(
    noteShared: Boolean = false,
    note: Note,
    state: SheetState,
    scope: CoroutineScope
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            NoteTimeline(
                noteShared = noteShared,
                note = note
            )
            NoteContent(
                note = note
            )
        }
    }
}

/**
 * The layout to display the note details
 *
 * @param note The note to display
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 */
@Composable
@NonRestartableComposable
fun NoteDetails(
    note: String,
    state: SheetState,
    scope: CoroutineScope
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            NoteContent(
                note = note
            )
        }
    }
}

/**
 * The layout to display the timeline events of the [note]
 *
 * @param noteShared Whether the note is shared with groups
 * @param note The note to display
 */
@Composable
@NonRestartableComposable
private fun NoteTimeline(
    noteShared: Boolean = false,
    note: Note
) {
    val noteCompleted = note.markedAsDone
    val yetToCompleteText = stringResource(Res.string.yet_to_complete)
    Text(
        modifier = Modifier
            .padding(
                start = 16.dp
            ),
        text = stringResource(Res.string.timeline),
        fontFamily = displayFontFamily,
        fontSize = 20.sp
    )
    val items = remember {
        listOf(
            note.creationDate.formatAsTimeString(),
            note.markAsDoneDate.formatAsTimeString(
                invalidTimeDefValue = yetToCompleteText
            )
        )
    }
    JetLimeColumn(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 10.dp
            ),
        style = if(noteCompleted)
            JetLimeDefaults.columnStyle()
        else {
            JetLimeDefaults.columnStyle(
                pathEffect = PathEffect.dashPathEffect(
                    intervals = FloatArray(items.size) { 10f },
                    phase = 0f,
                )
            )
        },
        itemsList = ItemsList(items),
    ) { _, date, position ->
        if(noteShared) {
            SharedNoteTimelineEvent(
                position = position,
                noteCompleted = noteCompleted,
                note = note,
                date = date
            )
        } else {
            NoteTimelineEvent(
                position = position,
                noteCompleted = noteCompleted,
                note = note,
                date = date
            )
        }
    }
    HorizontalDivider()
}

/**
 * The layout to display the details of timeline event of the [note]
 *
 * @param position The position in the timeline events
 * @param noteCompleted Whether the note is completed
 * @param note The note to display
 * @param date The date of the event to display
 */
@Composable
@NonRestartableComposable
private fun NoteTimelineEvent(
    position: EventPosition,
    noteCompleted: Boolean,
    note: Note,
    date: String
) {
    NoteTimelineEventContainer(
        position = position,
        noteCompleted = noteCompleted
    ) { startPosition ->
        Text(
            text = if(noteCompleted || startPosition)
                date
            else
                stringResource(Res.string.not_available),
            fontSize = 12.sp
        )
        Text(
            text = if(startPosition)
                stringResource(Res.string.note_created)
            else {
                if(noteCompleted) {
                    val completionDays = note.completionDays()
                    pluralStringResource(
                        resource = Res.plurals.note_completed_in,
                        quantity = completionDays,
                        completionDays
                    )
                } else
                    date
            },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontFamily = displayFontFamily
        )
    }
}

/**
 * The layout to display the details of timeline event of the [note] shared with groups
 *
 * @param position The position in the timeline events
 * @param noteCompleted Whether the note is completed
 * @param note The note to display
 * @param date The date of the event to display
 */
@Composable
@NonRestartableComposable
private fun SharedNoteTimelineEvent(
    position: EventPosition,
    noteCompleted: Boolean,
    note: Note,
    date: String
) {
    if(position.isNotEnd() || noteCompleted) {
        NoteTimelineEventContainer(
            position = position,
            noteCompleted = noteCompleted
        ) { startPosition ->
            Text(
                text = date,
                fontSize = 12.sp
            )
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val member = if(startPosition)
                    note.author
                else
                    note.markedAsDoneBy!!
                Thumbnail(
                    thumbnailData = member.profilePic,
                    contentDescription = "Member profile pic"
                )
                val memberName = member.completeName()
                Text(
                    text = if(startPosition) {
                        stringResource(
                            resource = Res.string.has_created_the_note,
                            memberName
                        )
                    } else {
                        val completionDays = note.completionDays()
                        pluralStringResource(
                            resource = Res.plurals.has_completed_the_note,
                            quantity = completionDays,
                            memberName, completionDays
                        )
                    },
                    fontFamily = displayFontFamily
                )
            }
        }
    }
}

/**
 * The container to display a timeline event
 *
 * @param position The position in the timeline events
 * @param noteCompleted Whether the note is completed
 * @param content The content of the event to display
 */
@Composable
@NonRestartableComposable
private fun NoteTimelineEventContainer(
    position: EventPosition,
    noteCompleted: Boolean,
    content: @Composable ColumnScope.(Boolean) -> Unit
) {
    val startPosition = position.isNotEnd()
    JetLimeEvent(
        style = JetLimeEventDefaults.eventStyle(
            position = position,
            pointType = if(!startPosition) {
                EventPointType.custom(
                    icon = rememberVectorPainter(
                        image = if(noteCompleted)
                            Icons.Default.CheckCircle
                        else
                            ClockLoader20
                    ),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else
                EventPointType.Default
        ),
    ) {
        Column (
            modifier = Modifier
                .heightIn(
                    min = 60.dp,
                    max = 75.dp
                ),
            content = {
                content.invoke(this, startPosition)
            }
        )
    }
}

/**
 * The content of the [note]
 *
 * @param note The note to display its content
 */
@Composable
@NonRestartableComposable
private fun NoteContent(
    note: Note
) {
    NoteContent(
        note = note.content
    )
}

/**
 * The content of the [note]
 *
 * @param note The content of the note to display
 */
@Composable
@NonRestartableComposable
private fun NoteContent(
    note: String
) {
    Text(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 16.dp
            ),
        text = stringResource(Res.string.content),
        fontFamily = displayFontFamily,
        fontSize = 20.sp
    )
    Text(
        modifier = Modifier
            .padding(
                horizontal = 16.dp
            )
            .padding(
                top = 5.dp,
                bottom = 16.dp
            )
            .verticalScroll(rememberScrollState()),
        text = note,
        textAlign = TextAlign.Justify
    )
}