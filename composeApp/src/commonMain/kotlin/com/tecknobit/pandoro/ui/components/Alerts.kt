package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.pandoro.SPLASHSCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.notes.presentation.NotesScreenViewModel
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.confirm
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.delete_group_text_dialog
import pandoro.composeapp.generated.resources.delete_item
import pandoro.composeapp.generated.resources.delete_note
import pandoro.composeapp.generated.resources.delete_note_text
import pandoro.composeapp.generated.resources.delete_project_text_dialog
import pandoro.composeapp.generated.resources.delete_warn_text
import pandoro.composeapp.generated.resources.dismiss
import pandoro.composeapp.generated.resources.logout
import pandoro.composeapp.generated.resources.logout_warn_text

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
        title = stringResource(Res.string.delete_item, project.name),
        text = stringResource(Res.string.delete_project_text_dialog),
        confirmAction = {
            viewModel.deleteProject(
                project = project,
                onDelete = onDelete
            )
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

@Composable
@NonRestartableComposable
fun DeleteNote(
    viewModel: NotesScreenViewModel,
    note: Note,
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
        title = stringResource(Res.string.delete_note),
        text = stringResource(Res.string.delete_note_text),
        confirmAction = {
            viewModel.deleteNote(
                note = note,
                onDelete = onDelete
            )
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

@Composable
@NonRestartableComposable
fun DeleteGroup(
    viewModel: GroupsScreenViewModel,
    group: Group,
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
        title = stringResource(Res.string.delete_item, group.name),
        text = stringResource(Res.string.delete_group_text_dialog),
        confirmAction = {
            viewModel.deleteGroup(
                group = group,
                onDelete = onDelete
            )
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

@Composable
@NonRestartableComposable
fun Logout(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        text = Res.string.logout_warn_text,
        confirmAction = {
            viewModel.clearSession {
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

@Composable
@NonRestartableComposable
fun DeleteAccount(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.delete,
        text = Res.string.delete_warn_text,
        confirmAction = {
            viewModel.deleteAccount {
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}