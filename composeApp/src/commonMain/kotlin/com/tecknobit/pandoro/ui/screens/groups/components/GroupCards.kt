@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.pandoro.ui.screens.groups.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.helpers.navToCreateGroupScreen
import com.tecknobit.pandoro.helpers.navToGroupScreen
import com.tecknobit.pandoro.ui.components.DeleteGroup
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import org.jetbrains.compose.resources.pluralStringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.members_number

/**
 * Card to display a group owned by the [com.tecknobit.pandoro.localUser]
 *
 * @param viewModel The support viewmodel for the screen
 * @param group The group to display
 */
@Composable
fun MyGroupCard(
    viewModel: GroupsScreenViewModel,
    group: Group
) {
    GroupCardContent(
        modifier = Modifier
            .size(
                width = 325.dp,
                height = 200.dp
            )
            .combinedClickable(
                onClick = {
                    navToGroup(
                        group = group
                    )
                },
                onLongClick = {
                    navToEditGroup(
                        group = group
                    )
                }
            ),
        viewModel = viewModel,
        group = group,
        memberAllowedToDelete = true,
        overlineText = {
            val membersNumber = group.members.size
            Text(
                text = pluralStringResource(
                    resource = Res.plurals.members_number,
                    quantity = membersNumber,
                    membersNumber
                ),
                fontSize = 12.sp
            )
        }
    )
}

/**
 * Card to display a group where the [com.tecknobit.pandoro.localUser] is a member
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param group The group to display
 */
@Composable
@NonRestartableComposable
fun GroupCard(
    modifier: Modifier,
    viewModel: GroupsScreenViewModel,
    group: Group
) {
    GroupCardContent(
        modifier = modifier
            .height(200.dp)
            .combinedClickable(
                onClick = {
                    navToGroup(
                        group = group
                    )
                },
                onLongClick = if(group.iAmAnAdmin()) {
                    {
                        navToEditGroup(
                            group = group
                        )
                    }
                } else
                    null
            ),
        viewModel = viewModel,
        group = group,
        memberAllowedToDelete = group.iAmTheAuthor(),
        overlineText = {
            val role = group.findMyRole()
            Text(
                text = role.asText(),
                color = role.color(),
                fontSize = 12.sp
            )
        }
    )
}

/**
 * The content of the group's cards
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param group The group to display
 * @param memberAllowedToDelete Whether the member is allowed to delete the [group]
 * @param overlineText The content to display as overline text
 */
@Composable
@NonRestartableComposable
private fun GroupCardContent(
    modifier: Modifier = Modifier,
    viewModel: GroupsScreenViewModel,
    group: Group,
    memberAllowedToDelete: Boolean,
    overlineText: @Composable () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column (
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            overlineText.invoke()
            GroupTitle(
                group = group
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = group.description,
                overflow = TextOverflow.Ellipsis,
                minLines = 3,
                maxLines = 3,
                fontSize = 14.sp
            )
        }
        CardFooter(
            viewModel = viewModel,
            group = group,
            memberAllowedToDelete = memberAllowedToDelete
        )
    }
}

/**
 * The title section of the group's cards
 *
 * @param modifier The modifier to apply to the component
 * @param group The group to display
 */
@Composable
@NonRestartableComposable
private fun GroupTitle(
    modifier: Modifier = Modifier,
    group: Group
) {
    Row (
        modifier = Modifier
            .heightIn(
                min = 35.dp
            ),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Thumbnail(
            thumbnailData = group.logo,
            contentDescription = "Group logo"
        )
        Text(
            modifier = modifier,
            text = group.name,
            fontFamily = displayFontFamily,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * The footer section of the group's cards
 *
 * @param viewModel The support viewmodel for the screen
 * @param group The group to display
 * @param memberAllowedToDelete Whether the member is allowed to delete the [group]
 */
@Composable
@NonRestartableComposable
private fun CardFooter(
    viewModel: GroupsScreenViewModel,
    group: Group,
    memberAllowedToDelete: Boolean
) {
    if(group.projects.isNotEmpty() || memberAllowedToDelete) {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(
                    min = 50.dp
                )
                .padding(
                    start = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectIcons(
                group = group
            )
            if(memberAllowedToDelete) {
                DeleteGroupButton(
                    viewModel = viewModel,
                    group = group
                )
            }
        }
    }
}

/**
 * The custom button to delete the [group]
 *
 * @param viewModel The support viewmodel for the screen
 * @param group The group to display
 */
@Composable
private fun DeleteGroupButton(
    viewModel: GroupsScreenViewModel,
    group: Group
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        val deleteGroup = remember { mutableStateOf(false) }
        IconButton(
            onClick = { deleteGroup.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        }
        DeleteGroup(
            viewModel = viewModel,
            group = group,
            show = deleteGroup,
            onDelete = {
                deleteGroup.value = false
                viewModel.refreshListsAfterDeletion()
            }
        )
    }
}

/**
 * Method to navigate to the [com.tecknobit.pandoro.ui.screens.creategroup.presenter.CreateGroupScreen]
 * to edit the [group]
 *
 * @param group The group to edit
 */
private fun navToEditGroup(
    group: Group
) {
    navToCreateGroupScreen(
        groupId = group.id
    )
}

/**
 * Method to navigate to the [com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen]
 * to display the [group]
 *
 * @param group The group to display in that screen
 */
private fun navToGroup(
    group: Group
) {
    navToGroupScreen(
        groupId = group.id
    )
}