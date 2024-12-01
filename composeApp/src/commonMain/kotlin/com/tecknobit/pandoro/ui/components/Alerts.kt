package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.confirm
import pandoro.composeapp.generated.resources.delete_project
import pandoro.composeapp.generated.resources.delete_project_text_dialog
import pandoro.composeapp.generated.resources.dismiss

@Composable
@NonRestartableComposable
fun DeleteProject(
    viewModel: ProjectsScreenViewModel,
    project: Project,
    show: MutableState<Boolean>,
    onDelete: () -> Unit
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = stringResource(Res.string.delete_project, project.name),
        text = stringResource(Res.string.delete_project_text_dialog),
        confirmAction = {
            viewModel.deleteProject(
                project = project
            )
            onDelete.invoke()
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}