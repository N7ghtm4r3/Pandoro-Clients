@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.components.DeleteUpdate
import com.tecknobit.pandoro.ui.icons.ClipboardList
import com.tecknobit.pandoro.ui.icons.ExportNotes
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.asText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.toColor
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import com.tecknobit.pandorocore.enums.UpdateStatus.SCHEDULED
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.changes_are_planned
import pandoro.composeapp.generated.resources.changes_completed_on
import pandoro.composeapp.generated.resources.update_completed_in
import pandoro.composeapp.generated.resources.update_completed_info

@Composable
@NonRestartableComposable
fun UpdateCard(
    modifier: Modifier = Modifier,
    viewModel: ProjectScreenViewModel,
    project: Project,
    update: ProjectUpdate
) {
    Card(
        modifier = modifier
            .fillMaxSize()
    ) {
        CardHeader(
            viewModel = viewModel,
            project = project,
            update = update
        )
        update.status.Content(
            update = update
        )
    }
}

@Composable
@NonRestartableComposable
private fun CardHeader(
    viewModel: ProjectScreenViewModel,
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
                .fillMaxWidth()
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
                )
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                UpdateActions(
                    viewModel = viewModel,
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
    update: ProjectUpdate
) {
    val widthSizeClass = getCurrentWidthSizeClass()
    when(widthSizeClass) {
        Expanded -> {
            ActionTexts(
                viewModel = viewModel,
                update = update
            )
        }
        else -> {
            ActionIcons(
                viewModel = viewModel,
                update = update
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun ActionIcons(
    viewModel: ProjectScreenViewModel,
    update: ProjectUpdate
) {
    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = ClipboardList,
            contentDescription = null
        )
    }
    IconButton(
        onClick = {

        }
    ) {
        Icon(
            imageVector = Icons.Default.Timeline,
            contentDescription = null
        )
    }
    IconButton(
        enabled = update.notes.isNotEmpty(),
        onClick = {

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
    DeleteUpdateAction(
        viewModel = viewModel,
        deleteUpdate = deleteUpdate,
        update = update
    )
}

@Composable
@NonRestartableComposable
private fun ActionTexts(
    viewModel: ProjectScreenViewModel,
    update: ProjectUpdate
) {
    TextButton(
        onClick = {

        }
    ) {
        Text(
            text = "View change notes"
        )
    }
    TextButton(
        onClick = {

        }
    ) {
        Text(
            text = "View timeline"
        )
    }
    TextButton(
        enabled = update.notes.isNotEmpty(),
        onClick = {

        }
    ) {
        Text(
            text = "Export notes"
        )
    }
    val deleteUpdate = remember { mutableStateOf(false) }
    TextButton(
        onClick = { deleteUpdate.value = true }
    ) {
        Text(
            text = "Delete",
            color = MaterialTheme.colorScheme.error
        )
    }
    DeleteUpdateAction(
        viewModel = viewModel,
        deleteUpdate = deleteUpdate,
        update = update
    )
}

@Composable
@NonRestartableComposable
private fun DeleteUpdateAction(
    viewModel: ProjectScreenViewModel,
    deleteUpdate: MutableState<Boolean>,
    update: ProjectUpdate
) {
    DeleteUpdate(
        viewModel = viewModel,
        show = deleteUpdate,
        update = update,
        onDelete = {
            deleteUpdate.value = false
            viewModel.updatesState.refresh()
        }
    )
}

@Composable
@NonRestartableComposable
private fun UpdateStatus.Content(
    update: ProjectUpdate
) {
    Column(
        modifier = Modifier
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
    }
}