@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.item.group.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewPageProgressIndicator
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.icons.FolderManaged
import com.tecknobit.pandoro.ui.screens.item.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandoro.ui.screens.shared.presentation.groups.GroupManagerViewModel
import com.tecknobit.pandoro.ui.theme.green
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.invite
import pandoro.composeapp.generated.resources.remove

/**
 * The actions can be executed on a group such add members or share projects
 *
 * @param viewModel The support viewmodel of the screen
 * @param projectsOnDismissAction The action to execute when the sharing of the projects ends
 * @param userCanAddProjects Whether the current user can add projects to the group
 * @param membersOnDismissAction The action when the addition of the members ends
 * @param userCanAddMembers Whether the user can add new members to the group
 */
@Composable
fun GroupActions(
    viewModel: GroupManagerViewModel,
    projectsOnDismissAction: (() -> Unit)? = null,
    userCanAddProjects: Boolean = true,
    membersOnDismissAction: (() -> Unit)? = null,
    userCanAddMembers: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        if(viewModel.userProjects.isNotEmpty() && userCanAddProjects) {
            AttachProjectsButton(
                viewModel = viewModel,
                projectsOnDismissAction = projectsOnDismissAction
            )
        }
        val candidatesMemberAvailable by viewModel.candidatesMemberAvailable.collectAsState(
            initial = false
        )
        if(candidatesMemberAvailable && userCanAddMembers) {
            val membersSheetState = rememberModalBottomSheetState()
            val membersScope = rememberCoroutineScope()
            FloatingActionButton(
                onClick = {
                    membersScope.launch {
                        membersSheetState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = null
                )
            }
            ManageGroupMembers(
                state = membersSheetState,
                scope = membersScope,
                viewModel = viewModel,
                extraOnDismissAction = membersOnDismissAction
            )
        }
    }
}

/**
 * The custom button to attach projects to the group
 *
 * @param viewModel The support viewmodel of the screen
 * @param projectsOnDismissAction The action to execute when the sharing of the projects ends
 */
@Composable
private fun AttachProjectsButton(
    viewModel: GroupManagerViewModel,
    projectsOnDismissAction: (() -> Unit)? = null,
) {
    val projectsSheetState = rememberModalBottomSheetState()
    val projectsScope = rememberCoroutineScope()
    SmallFloatingActionButton(
        onClick = {
            projectsScope.launch {
                projectsSheetState.show()
            }
        }
    ) {
        Icon(
            imageVector = FolderManaged,
            contentDescription = null
        )
    }
    GroupProjects(
        state = projectsSheetState,
        scope = projectsScope,
        viewModel = viewModel,
        projectsOnDismissAction = projectsOnDismissAction
    )
}

/**
 * The layout useful to display the projects shared with the group and allowing addition or to remove
 * projects
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param viewModel The support viewmodel of the screen
 * @param projectsOnDismissAction The action to execute when the sharing of the projects ends
 */
@Composable
private fun GroupProjects(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: GroupManagerViewModel,
    projectsOnDismissAction: (() -> Unit)? = null,
) {
    GroupProjectsCandidate(
        extraOnDismissAction = projectsOnDismissAction,
        state = state,
        scope = scope,
        projects = viewModel.userProjects,
        trailingContent = { project ->
            if (viewModel.userProjects.any { checkProject -> checkProject.id == project.id }) {
                var added by remember {
                    mutableStateOf(viewModel.candidateProjects.contains(project.id))
                }
                Checkbox(
                    checked = added,
                    onCheckedChange = { selected ->
                        viewModel.manageProjectCandidate(
                            project = project
                        )
                        added = selected
                    }
                )
            }
        }
    )
}

/**
 * The layout useful to manage the members of the group
 *
 * @param viewModel The support viewmodel of the screen
 * @param extraOnDismissAction The action to execute when [ModalBottomSheet] dismissed
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 */
@Composable
private fun ManageGroupMembers(
    viewModel: GroupManagerViewModel,
    extraOnDismissAction: (() -> Unit)?,
    state: SheetState,
    scope: CoroutineScope
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                extraOnDismissAction?.invoke()
                scope.launch {
                    state.hide()
                }
            }
        ) {
            GroupMembers(
                viewModel = viewModel
            )
        }
    }
}

/**
 * The members list of the group
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel of the screen
 */
@Composable
fun GroupMembers(
    modifier: Modifier = Modifier,
    viewModel: GroupManagerViewModel
) {
    PaginatedLazyColumn(
        modifier = modifier
            .animateContentSize(),
        paginationState = viewModel.candidateMembersState,
        contentPadding = PaddingValues(
            vertical = 10.dp
        ),
        firstPageEmptyIndicator = { viewModel.noCandidatesAvailable() },
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        viewModel.candidatesAvailable()
        items(
            items = viewModel.candidateMembersState.allItems!!,
            key = { member -> member.id }
        ) { member ->
            GroupMember(
                viewModel = viewModel,
                member = member
            )
        }
    }
}

/**
 * The member details
 *
 * @param viewModel The support viewmodel of the screen
 * @param member The member to display his/her details
 */
@Composable
@NonRestartableComposable
private fun GroupMember(
    viewModel: GroupManagerViewModel,
    member: GroupMember
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            Thumbnail(
                size = 50.dp,
                thumbnailData = member.profilePic,
                contentDescription = "Member profile pic"
            )
        },
        overlineContent = {
            val role = member.role
            Text(
                text = role.asText(),
                color = role.color()
            )
        },
        headlineContent = {
            Text(
                text = member.completeName()
            )
        },
        supportingContent = {
            Text(
                text = member.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            if(viewModel.groupMembers.contains(member)) {
                Button(
                    modifier = Modifier
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    shape = RoundedCornerShape(
                        size = 5.dp
                    ),
                    onClick = { viewModel.groupMembers.remove(member) }
                ) {
                    Text(
                        text = stringResource(Res.string.remove),
                        fontSize = 12.sp
                    )
                }
            } else {
                Button(
                    modifier = Modifier
                        .height(35.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = green()
                    ),
                    shape = RoundedCornerShape(
                        size = 5.dp
                    ),
                    onClick = { viewModel.groupMembers.add(member) }
                ) {
                    Text(
                        text = stringResource(Res.string.invite),
                        fontSize = 12.sp
                    )
                }
            }
        }
    )
    HorizontalDivider(
        modifier = Modifier
            .padding(
                start = 65.dp
            )
    )
}