package com.tecknobit.pandoro.ui.screens.group.presenter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.CREATE_GROUP_SCREEN
import com.tecknobit.pandoro.getCurrentSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.ChangeMemberRole
import com.tecknobit.pandoro.ui.components.DeleteGroup
import com.tecknobit.pandoro.ui.components.LeaveGroup
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.group.components.GroupActions
import com.tecknobit.pandoro.ui.screens.group.components.MembersTable
import com.tecknobit.pandoro.ui.screens.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.screens.ItemScreen
import com.tecknobit.pandorocore.enums.InvitationStatus.PENDING

class GroupScreen(
    groupId: String
) : ItemScreen<Group, GroupScreenViewModel>(
    viewModel = GroupScreenViewModel(
        groupId = groupId
    ),
    bottomPadding = 0.dp
) {

    @Composable
    @NonRestartableComposable
    override fun ItemTitle() {
        ScreenTitle(
            navBackAction = { navigator.goBack() },
            title = item.value!!.name
        )
    }

    override fun getThumbnailData(): String {
        return item.value!!.logo
    }

    @Composable
    @NonRestartableComposable
    override fun ItemRelationshipItems() {
        ProjectIcons(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                ),
            group = item.value!!
        )
    }

    override fun getItemDescription(): String {
        return item.value!!.description
    }

    override fun onEdit() {
        navigator.navigate("$CREATE_GROUP_SCREEN/${item.value!!.id}")
    }

    @Composable
    @NonRestartableComposable
    override fun DeleteItemAction(
        delete: MutableState<Boolean>
    ) {
        DeleteGroup(
            viewModel = viewModel!!,
            group = item.value!!,
            show = delete,
            onDelete = {
                delete.value = false
                navigator.goBack()
            }
        )
    }

    override fun getItemAuthor(): PandoroUser {
        return item.value!!.author
    }

    @Composable
    @NonRestartableComposable
    override fun ExtraAction() {
        if(!iAmTheAuthor()) {
            val leaveGroup = remember { mutableStateOf(false) }
            IconButton(
                onClick = { leaveGroup.value = true }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
            LeaveGroup(
                viewModel = viewModel!!,
                show = leaveGroup,
                group = item.value!!
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ScreenContent() {
        val windowSizeClass = getCurrentSizeClass()
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass
        when {
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Expanded -> {
                MembersTable(
                    viewModel = viewModel!!
                )
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Medium -> {
                MembersColumn()
            }
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Medium -> {
                MembersTable(
                    viewModel = viewModel!!
                )
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded -> {
                MembersColumn()
            }
            else -> MembersColumn()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun MembersColumn() {
        LazyColumn(
            modifier = Modifier
                .padding(
                    top = 6.dp
                )
        ) {
            items(
                items = item.value!!.members,
                key = { member -> member.id }
            ) { member ->
                Member(
                    member = member,
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Member(
        member: GroupMember
    ) {
        val authorizedToOpe = item.value!!.checkRolePermissions(
            member = member
        )
        val changeMemberRole = remember { mutableStateOf(false) }
        ListItem(
            modifier = Modifier
                .clickable (
                    enabled = authorizedToOpe && member.joined()
                ) {
                    changeMemberRole.value = true
                },
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
            ),
            leadingContent = {
                Thumbnail(
                    size = 50.dp,
                    thumbnailData = member.profilePic,
                    contentDescription = "Member profile pic"
                )
            },
            overlineContent = {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    val role = member.role
                    Text(
                        text = role.asText(),
                        color = role.color()
                    )
                    if(!member.joined()) {
                        Text(
                            text = PENDING.asText(),
                            color = PENDING.color(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            headlineContent = {
                Text(
                    text = member.completeName(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            supportingContent = {
                Text(
                    text = member.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = if(authorizedToOpe) {
                {
                    IconButton(
                        onClick = {
                            viewModel!!.removeMember(
                                member = member
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonRemove,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            } else
                null
        )
        HorizontalDivider()
        ChangeMemberRole(
            viewModel = viewModel!!,
            show = changeMemberRole,
            member = member
        )
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        GroupActions(
            viewModel = viewModel!!,
            userCanAddProjects = item.value!!.iAmAnAdmin(),
            userCanAddMembers = item.value!!.iAmAMaintainer()
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveGroup()
        viewModel!!.retrieveUserProjects()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        item = viewModel!!.group.collectAsState()
        viewModel!!.candidateMembersAvailable = remember {
            mutableStateOf(viewModel!!.retrieveCandidateMembers().isNotEmpty())
        }
    }

}