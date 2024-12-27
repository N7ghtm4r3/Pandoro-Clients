@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.group.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
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
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandoro.ui.theme.Green
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.invite
import pandoro.composeapp.generated.resources.remove

@Composable
@NonRestartableComposable
fun GroupActions(
    viewModel: GroupManagerViewModel,
    userCanAddProjects: Boolean = true,
    groupMembersOnDismissAction: (() -> Unit)? = null,
    userCanAddMembers: Boolean = true
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        if(viewModel.userProjects.isNotEmpty() && userCanAddProjects) {
            AttachProjectsButton(
                viewModel = viewModel
            )
        }
        if(viewModel.candidatesMemberAvailable.value && userCanAddMembers) {
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
                modalBottomSheetState = membersSheetState,
                scope = membersScope,
                viewModel = viewModel,
                extraOnDismissAction = groupMembersOnDismissAction
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun AttachProjectsButton(
    viewModel: GroupManagerViewModel
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
            imageVector = Icons.Default.CreateNewFolder,
            contentDescription = null
        )
    }
    GroupProjects(
        modalBottomSheetState = projectsSheetState,
        scope = projectsScope,
        viewModel = viewModel
    )
}

@Composable
@NonRestartableComposable
private fun GroupProjects(
    modalBottomSheetState: SheetState,
    scope: CoroutineScope,
    viewModel: GroupManagerViewModel
) {
    val projects: MutableList<Project> = remember { mutableListOf() }
    LaunchedEffect(Unit) {
        projects.addAll(viewModel.userProjects + viewModel.groupProjects)
    }
    GroupProjectsCandidate(
        modalBottomSheetState = modalBottomSheetState,
        scope = scope,
        projects = projects.distinctBy { project -> project.id },
        trailingContent = { project ->
            if (viewModel.userProjects.any { checkProject -> checkProject.id == project.id }) {
                var added by remember {
                    mutableStateOf(
                        viewModel.candidateProjects.contains(project.id) ||
                                viewModel.groupProjects.contains(project)
                    )
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

@Composable
@NonRestartableComposable
private fun ManageGroupMembers(
    viewModel: GroupManagerViewModel,
    extraOnDismissAction: (() -> Unit)?,
    modalBottomSheetState: SheetState,
    scope: CoroutineScope
) {
    if(modalBottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                extraOnDismissAction?.invoke()
                scope.launch {
                    modalBottomSheetState.hide()
                }
            }
        ) {
            GroupMembers(
                viewModel = viewModel
            )
        }
    }
}

@Composable
@NonRestartableComposable
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
                        containerColor = Green()
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