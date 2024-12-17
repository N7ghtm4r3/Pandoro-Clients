package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.SPLASHSCREEN
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.check_change_notes_message
import pandoro.composeapp.generated.resources.confirm
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.delete_group_text_dialog
import pandoro.composeapp.generated.resources.delete_item
import pandoro.composeapp.generated.resources.delete_note
import pandoro.composeapp.generated.resources.delete_note_text
import pandoro.composeapp.generated.resources.delete_project_text_dialog
import pandoro.composeapp.generated.resources.delete_update_text_dialog
import pandoro.composeapp.generated.resources.delete_warn_text
import pandoro.composeapp.generated.resources.dismiss
import pandoro.composeapp.generated.resources.logout
import pandoro.composeapp.generated.resources.logout_warn_text
import pandoro.composeapp.generated.resources.not_all_the_change_notes_are_done

val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 18.sp
)

@Composable
@NonRestartableComposable
fun DeleteProject(
    viewModel: EquinoxViewModel,
    project: Project,
    show: MutableState<Boolean>,
    deleteRequest: (Project) -> Unit
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
        titleStyle = titleStyle,
        text = stringResource(Res.string.delete_project_text_dialog),
        confirmAction = {
            deleteRequest.invoke(project)
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

@Composable
@NonRestartableComposable
fun DeleteUpdate(
    viewModel: ProjectScreenViewModel,
    update: ProjectUpdate,
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
        title = stringResource(Res.string.delete_item, update.targetVersion.asVersionText()),
        titleStyle = titleStyle,
        text = stringResource(Res.string.delete_update_text_dialog),
        confirmAction = {
            viewModel.deleteUpdate(
                update = update,
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
    viewModel: EquinoxViewModel,
    update: ProjectUpdate? = null,
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
        titleStyle = titleStyle,
        text = stringResource(Res.string.delete_note_text),
        confirmAction = {
            (viewModel as NotesManager).deleteNote(
                update = update,
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
    viewModel: EquinoxViewModel,
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
        titleStyle = titleStyle,
        text = stringResource(Res.string.delete_group_text_dialog),
        confirmAction = {
            (viewModel as GroupDeleter).deleteGroup(
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
        titleStyle = titleStyle,
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
        titleStyle = titleStyle,
        text = Res.string.delete_warn_text,
        confirmAction = {
            viewModel.deleteAccount {
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

@Composable
@NonRestartableComposable
fun NotAllChangeNotesCompleted(
    viewModel: ProjectScreenViewModel,
    show: MutableState<Boolean>,
    update: ProjectUpdate
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Warning,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.not_all_the_change_notes_are_done,
        titleStyle = titleStyle,
        text = Res.string.check_change_notes_message,
        confirmAction = {
            viewModel.publishUpdate(
                update = update
            )
            show.value = false
        }
    )
}