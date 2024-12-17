@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTooltipState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.CREATE_CHANGE_NOTE_SCREEN
import com.tecknobit.pandoro.copyToClipboard
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteUpdate
import com.tecknobit.pandoro.ui.components.NotAllChangeNotesCompleted
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.icons.ClipboardList
import com.tecknobit.pandoro.ui.icons.ClipboardMinus
import com.tecknobit.pandoro.ui.icons.ExportNotes
import com.tecknobit.pandoro.ui.screens.PandoroScreen.Companion.FORM_CARD_HEIGHT
import com.tecknobit.pandoro.ui.screens.notes.components.ChangeNoteCard
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.asText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.toColor
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandoro.ui.theme.Yellow
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.changes_are_planned
import pandoro.composeapp.generated.resources.changes_completed_on
import pandoro.composeapp.generated.resources.notes_formatted_in_markdown_copied
import pandoro.composeapp.generated.resources.publish_update
import pandoro.composeapp.generated.resources.start_development
import pandoro.composeapp.generated.resources.update_completed_in
import pandoro.composeapp.generated.resources.update_completed_info

@Composable
@NonRestartableComposable
fun UpdateCard(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: ProjectUpdate,
    viewChangeNotesFlag: Boolean
) {
    Card(
        modifier = modifier
            .fillMaxSize()
    ) {
        val viewChangeNotes = remember { mutableStateOf(viewChangeNotesFlag) }
        CardHeader(
            viewModel = viewModel,
            viewChangeNotes = viewChangeNotes,
            project = project,
            update = update
        )
        update.status.Content(
            viewModel = viewModel,
            viewChangeNotes = viewChangeNotes,
            project = project,
            update = update
        )
    }
}

@Composable
@NonRestartableComposable
private fun CardHeader(
    viewModel: ProjectScreenViewModel,
    viewChangeNotes: MutableState<Boolean>,
    project: Project,
    update: ProjectUpdate
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(0.8f)
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        start = 10.dp
                    ),
                text = "${project.name} ${update.targetVersion.asVersionText()}",
                fontSize = 12.sp
            )
            Row (
                modifier = Modifier
                    .padding(
                        start = 10.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val status = update.status
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(10.dp)
                        .background(status.toColor())
                ) {}
                Text(
                    text = status.asText(),
                    fontFamily = displayFontFamily
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(
                    when(getCurrentWidthSizeClass()) {
                        Expanded -> 2f
                        else -> 1f
                    }
                ),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UpdateActions(
                    viewModel = viewModel,
                    viewChangeNotes = viewChangeNotes,
                    project = project,
                    update = update
                )
            }
        }
    }
    HorizontalDivider()
}

@Composable
@NonRestartableComposable
private fun UpdateActions(
    viewModel: ProjectScreenViewModel,
    viewChangeNotes: MutableState<Boolean>,
    project: Project,
    update: ProjectUpdate
) {
    IconButton(
        onClick = {
            viewChangeNotes.value = !viewChangeNotes.value
            if(viewChangeNotes.value)
                preventScreenSleep()
            else
                allowsScreenSleep()
        }
    ) {
        Icon(
            imageVector = if(viewChangeNotes.value)
                ClipboardMinus
            else
                ClipboardList,
            contentDescription = null
        )
    }
    val timelineBottomSheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val timelineScope = rememberCoroutineScope()
    IconButton(
        onClick = {
            timelineScope.launch {
                timelineBottomSheet.show()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Timeline,
            contentDescription = null
        )
    }
    ViewTimeline(
        state = timelineBottomSheet,
        scope = timelineScope,
        project = project,
        update = update
    )
    val notesFormatted = stringResource(Res.string.notes_formatted_in_markdown_copied)
    IconButton(
        enabled = update.notes.isNotEmpty(),
        onClick = {
            copyToClipboard(
                content = formatNotesAsMarkdown(
                    update = update
                ),
                onCopy = {
                    viewModel.showSnackbarMessage(
                        message = notesFormatted
                    )
                }
            )
        }
    ) {
        Icon(
            imageVector = ExportNotes,
            contentDescription = null
        )
    }
    val deleteUpdate = remember { mutableStateOf(false) }
    IconButton(
        onClick = { deleteUpdate.value = true }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
    DeleteUpdate(
        viewModel = viewModel,
        show = deleteUpdate,
        update = update,
        onDelete = { deleteUpdate.value = false }
    )
}

@Composable
@NonRestartableComposable
private fun ViewTimeline(
    state: SheetState,
    scope: CoroutineScope,
    project: Project,
    update: ProjectUpdate
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            if(project.isSharedWithGroups()) {
                SharedUpdateTimeline(
                    update = update
                )
            } else {
                UpdateTimeline(
                    update = update
                )
            }
        }
    }
}


/**
 * Method to format on **markdown** the notes of an update
 *
 * @param update The update from format the notes
 *
 * @return the notes of an update formatted as markdown as [String]
 */
private fun formatNotesAsMarkdown(update: ProjectUpdate): String {
    val builder = StringBuilder()
    update.notes.forEach { note ->
        builder.append("- ").append(note.content).append("\n")
    }
    return builder.toString()
}

@Composable
@NonRestartableComposable
private fun UpdateStatus.Content(
    viewModel: ProjectScreenViewModel,
    viewChangeNotes: MutableState<Boolean>,
    project: Project,
    update: ProjectUpdate
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                all = 10.dp
            )
    ) {
        val notesNumber = update.notes.size
        when(this@Content) {
            SCHEDULED -> {
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.changes_are_planned,
                        quantity = notesNumber,
                        notesNumber
                    ),
                    fontSize = 14.sp
                )
            }
            IN_DEVELOPMENT -> {
                val completedChangeNotes = update.completedChangeNotes()
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = pluralStringResource(
                            resource = Res.plurals.changes_completed_on,
                            quantity = completedChangeNotes,
                            completedChangeNotes, notesNumber
                        ),
                        fontSize = 14.sp
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        AnimatedVisibility(
                            visible = update.allChangeNotesCompleted()
                        ) {
                            val state = rememberTooltipState()
                            val scope = rememberCoroutineScope()
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text(
                                            text = stringResource(Res.string.update_completed_info)
                                        )
                                    }
                                },
                                state = state
                            ) {
                                IconButton(
                                    modifier = Modifier
                                        .size(25.dp),
                                    onClick = {
                                        scope.launch {
                                            state.show()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.NewReleases,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            PUBLISHED -> {
                val completionDays = update.developmentDays()
                Text(
                    text = pluralStringResource(
                        resource = Res.plurals.update_completed_in,
                        quantity = completionDays,
                        completionDays
                    ),
                    fontSize = 14.sp
                )
            }
        }
        ViewChangeNotes(
            viewChangeNotes = viewChangeNotes,
            viewModel = viewModel,
            project = project,
            update = update
        )
        when(this@Content) {
            SCHEDULED -> {
                ActionButton(
                    color = Yellow(),
                    action = {
                        viewModel.startUpdate(
                            update = update
                        )
                    },
                    text = Res.string.start_development
                )
            }
            IN_DEVELOPMENT -> {
                val showWarningMessage = remember { mutableStateOf(false) }
                ActionButton(
                    color = Green(),
                    action = {
                        if(update.allChangeNotesCompleted()) {
                            viewModel.publishUpdate(
                                update = update
                            )
                        } else
                            showWarningMessage.value = true
                    },
                    text = Res.string.publish_update
                )
                NotAllChangeNotesCompleted(
                    viewModel = viewModel,
                    show = showWarningMessage,
                    update = update
                )
            }
            else -> {}
        }
    }
}

@Composable
@NonRestartableComposable
private fun ColumnScope.ActionButton(
    color: Color,
    action: () -> Unit,
    text: StringResource
) {
    Button(
        modifier = Modifier
            .align(Alignment.End),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        ),
        shape = RoundedCornerShape(
            size = 10.dp
        ),
        onClick = action
    ) {
        Text(
            text = stringResource(text),
        )
    }
}

@Composable
@NonRestartableComposable
private fun ViewChangeNotes(
    viewChangeNotes: MutableState<Boolean>,
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: ProjectUpdate
) {
    AnimatedVisibility(
        visible = viewChangeNotes.value
    ) {
        Column {
            val widthSizeClass = getCurrentWidthSizeClass()
            when(widthSizeClass) {
                Compact -> {
                    LazyColumn(
                        modifier = Modifier
                            .heightIn(
                                max = FORM_CARD_HEIGHT
                            )
                            .animateContentSize(),
                        contentPadding = PaddingValues(
                            vertical = 10.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = update.notes,
                            key = { note -> note.id }
                        ) { note ->
                            ChangeNoteCard(
                                modifier = Modifier
                                    .height(
                                        height = 175.dp
                                    ),
                                viewModel = viewModel,
                                project = project,
                                update = update,
                                note = note
                            )
                        }
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .heightIn(
                                max = FORM_CARD_HEIGHT
                            )
                            .animateContentSize(),
                        contentPadding = PaddingValues(
                            vertical = 10.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = update.notes,
                            key = { note -> note.id }
                        ) { note ->
                            ChangeNoteCard(
                                modifier = Modifier
                                    .height(
                                        height = 175.dp
                                    ),
                                viewModel = viewModel,
                                update = update,
                                project = project,
                                note = note
                            )
                        }
                    }
                }
            }
            if(update.status != PUBLISHED) {
                SmallFloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.End),
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        navigator.navigate(
                            route = "$CREATE_CHANGE_NOTE_SCREEN/${update.id}/${update.targetVersion}"
                        )
                    }
                ) {
                    Icon(
                        imageVector = AddNotes,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

expect fun preventScreenSleep()

expect fun allowsScreenSleep()