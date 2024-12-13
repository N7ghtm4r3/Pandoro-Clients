package com.tecknobit.pandoro.ui.screens.project.components

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
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

@Composable
@NonRestartableComposable
fun UpdateCard(
    modifier: Modifier,
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
        Text(
            text = "Sono previsti 19 cambiamenti"
        )
        if(update.status == UpdateStatus.IN_DEVELOPMENT) {
            Text(
                text = "In sviluppo da 19 giorni"
            )
            Text(
                text = "Avanzamento 1 su 9 cambiamenti completati",
                fontFamily = displayFontFamily
            )
        }
        if(update.status == UpdateStatus.PUBLISHED) {
            Text(
                text = "Completato in 19 giorni"
            )
        }
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