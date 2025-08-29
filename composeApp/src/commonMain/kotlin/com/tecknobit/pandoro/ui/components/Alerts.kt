package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.helpers.navToSplashscreen
import com.tecknobit.pandoro.ui.screens.item.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.lists.groups.data.Group
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.screens.item.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.lists.projects.data.Project
import com.tecknobit.pandoro.ui.screens.lists.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.lists.projects.data.Update
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandoro.ui.screens.shared.presentation.NotesManager
import com.tecknobit.pandoro.ui.screens.shared.presentation.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandorocore.enums.Role
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.change_role
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
import pandoro.composeapp.generated.resources.leave_group
import pandoro.composeapp.generated.resources.leave_group_text
import pandoro.composeapp.generated.resources.logout
import pandoro.composeapp.generated.resources.logout_warn_text
import pandoro.composeapp.generated.resources.not_all_the_change_notes_are_done

/**
 * `titleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 18.sp
)

/**
 * Alert to warn about the project deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project to delete
 * @param show Whether the alert is shown
 * @param deleteRequest The request to delete the project
 */
@Composable
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

/**
 * Alert to warn about the update deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param update The update to delete
 * @param show Whether the alert is shown
 * @param onDelete The action to execute when the update has been deleted
 */
@Composable
fun DeleteUpdate(
    viewModel: ProjectScreenViewModel,
    update: Update,
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

/**
 * Alert to warn about the note deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param update The update owner of the change note
 * @param note The note to delete
 * @param show Whether the alert is shown
 * @param onDelete The action to execute when the note has been deleted
 */
@Composable
fun DeleteNote(
    viewModel: EquinoxViewModel,
    update: Update? = null,
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

/**
 * Alert to warn about the group deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param group The group to delete
 * @param show Whether the alert is shown
 * @param onDelete The action to execute when the group has been deleted
 */
@Composable
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
                onDelete = onDelete,
                onFailure = { viewModel.showSnackbarMessage(it) }
            )
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
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
                navToSplashscreen()
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
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
                navToSplashscreen()
            }
        }
    )
}

/**
 * Alert to warn about a publishing of an update where not all the change notes has been completed
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 * @param update The update owner
 */
@Composable
fun NotAllChangeNotesCompleted(
    viewModel: ProjectScreenViewModel,
    show: MutableState<Boolean>,
    update: Update
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

/**
 * Alert to warn about the leaving group action
 *
 * @param viewModel The support viewmodel for the screen
 * @param group The group from leave
 * @param show Whether the alert is shown
 */
@Composable
fun LeaveGroup(
    viewModel: GroupScreenViewModel,
    group: Group,
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
        title = stringResource(Res.string.leave_group, group.name),
        titleStyle = titleStyle,
        text = stringResource(Res.string.leave_group_text),
        confirmAction = {
            viewModel.leaveGroup()
        },
        confirmText = stringResource(Res.string.confirm),
        dismissText = stringResource(Res.string.dismiss)
    )
}

/**
 * Alert to change the member role
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 * @param member The member to change his/her role
 */
@Composable
fun ChangeMemberRole(
    viewModel: GroupScreenViewModel,
    show: MutableState<Boolean>,
    member: GroupMember
) {
    var role by remember { mutableStateOf(member.role) }
    EquinoxAlertDialog(
        icon = Icons.Default.Badge,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.change_role,
        titleStyle = titleStyle,
        text = {
            LazyColumn(
                modifier = Modifier
                    .selectableGroup()
            ) {
                items(
                    items = Role.entries,
                    key = { role -> role }
                ) { roleOption ->
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = role == roleOption,
                            onClick = {
                                role = roleOption
                            }
                        )
                        Text(
                            text = roleOption.asText(),
                            color = roleOption.color()
                        )
                    }
                }
            }
        },
        confirmAction = {
            viewModel.changeMemberRole(
                member = member,
                role = role,
                onChange = {
                    show.value = false
                }
            )
        }
    )
}