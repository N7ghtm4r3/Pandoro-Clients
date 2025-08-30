@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.item.group.presenter

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.pandoro.helpers.navToCreateGroupScreen
import com.tecknobit.pandoro.helpers.navigator
import com.tecknobit.pandoro.ui.components.ChangeMemberRole
import com.tecknobit.pandoro.ui.components.DeleteGroup
import com.tecknobit.pandoro.ui.components.LeaveGroup
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.item.group.components.GroupActions
import com.tecknobit.pandoro.ui.screens.item.group.components.MembersTable
import com.tecknobit.pandoro.ui.screens.item.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.lists.groups.data.Group
import com.tecknobit.pandoro.ui.screens.item.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.item.ItemScreen
import com.tecknobit.pandoro.ui.screens.shared.presenters.PandoroScreen
import com.tecknobit.pandorocore.enums.InvitationStatus.PENDING

/**
 * The [GroupScreen] displays the details of a group and allow to manage that group
 *
 * @param groupId The identifier of the group to display
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see ItemScreen
 */
class GroupScreen(
    groupId: String
) : ItemScreen<Group, GroupScreenViewModel>(
    viewModel = GroupScreenViewModel(
        groupId = groupId
    ),
    bottomPadding = 0.dp
) {

    /**
     * `candidatesAvailable` whether there are any candidates available to be added in the group
     */
    private lateinit var candidatesAvailable: State<Boolean>

    /**
     * Method used to retrieve a [SessionFlowState] instance used by the inheritors screens
     *
     * @return the state instance as [SessionFlowState]
     */
    @Returner
    override fun sessionFlowState(): SessionFlowState {
        return viewModel.sessionFlowState
    }

    /**
     * The title of the screen
     */
    @Composable
    @ScreenSection
    override fun ItemTitle() {
        ScreenTitle(
            navBackAction = { navigator.popBackStack() },
            title = item.value!!.name
        )
    }

    /**
     * Method to get the thumbnail data from the item
     *
     * @return the thumbnail data of the item as nullable [String]
     */
    override fun getThumbnailData(): String {
        return item.value!!.logo
    }

    /**
     * The related items of the [item] such groups or projects
     */
    @Composable
    override fun ItemRelationshipItems() {
        ProjectIcons(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                ),
            group = item.value!!
        )
    }

    /**
     * Method to get the description of the item
     *
     * @return the description of the item as [String]
     */
    override fun getItemDescription(): String {
        return item.value!!.description
    }

    /**
     * The action to execute when the [item] has been edited
     */
    override fun onEdit() {
        navToCreateGroupScreen(
            groupId = item.value!!.id
        )
    }

    /**
     * The action to execute when the [item] has been requested to delete
     *
     * @param delete Whether the warn alert about the deletion is shown
     */
    @Composable
    @NonRestartableComposable
    override fun DeleteItemAction(
        delete: MutableState<Boolean>
    ) {
        DeleteGroup(
            viewModel = viewModel,
            group = item.value!!,
            show = delete,
            onDelete = {
                delete.value = false
                navigator.popBackStack()
            }
        )
    }

    /**
     * Method to get the author of the [item]
     *
     * @return the author of the item as [PandoroUser]
     */
    override fun getItemAuthor(): PandoroUser {
        return item.value!!.author
    }

    /**
     * Extra action component to allow the user to execute an action on the [item] such the
     * leaving from a group
     */
    @Composable
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
                viewModel = viewModel,
                show = leaveGroup,
                group = item.value!!
            )
        }
    }

    /**
     * The related content of the screen
     */
    @Composable
    @LayoutCoordinator
    override fun ItemContent() {
        ResponsiveContent(
            onExpandedSizeClass = {
                MembersTable(
                    viewModel = viewModel,
                    group = item
                )
            },
            onMediumSizeClass = { MembersColumn() },
            onCompactSizeClass = { MembersColumn() }
        )
    }

    /**
     * The members displayed in a [LazyColumn] format for the mobile devices for example
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
    )
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

    /**
     * The member item details
     *
     * @param member The member to display
     */
    @Composable
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
                            viewModel.removeMember(
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
            viewModel = viewModel,
            show = changeMemberRole,
            member = member
        )
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    override fun FabAction() {
        GroupActions(
            viewModel = viewModel,
            userCanAddProjects = item.value!!.iAmAnAdmin() && viewModel.userProjects.isNotEmpty(),
            projectsOnDismissAction = { viewModel.editProjects() },
            userCanAddMembers = item.value!!.iAmAMaintainer() && candidatesAvailable.value,
            membersOnDismissAction = { viewModel.addMembers() }
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveGroup()
        viewModel.retrieveUserProjects()
        viewModel.countCandidatesMember()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.sessionFlowState = rememberSessionFlowState()
        item = viewModel.group.collectAsState()
        candidatesAvailable = viewModel.candidatesMemberAvailable.collectAsState(
            initial = false
        )
    }

}