package com.tecknobit.pandoro.ui.screens.group.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.ApplicationTheme.Auto
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.ApplicationTheme.Dark
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import com.tecknobit.pandorocore.enums.InvitationStatus
import com.tecknobit.pandorocore.enums.Role
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.actions
import pandoro.composeapp.generated.resources.email
import pandoro.composeapp.generated.resources.member
import pandoro.composeapp.generated.resources.role
import pandoro.composeapp.generated.resources.status

private val maintainerHeaders = listOf(
    Res.string.member,
    Res.string.email,
    Res.string.role,
    Res.string.status,
    Res.string.actions
)

private val developerHeaders = listOf(
    Res.string.member,
    Res.string.email,
    Res.string.role,
    Res.string.status
)

@Composable
@NonRestartableComposable
fun MembersTable(
    viewModel: GroupScreenViewModel
) {
    val currentUserIsMaintainer = viewModel.group.value!!.iAmAMaintainer()
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(
                    max = if(currentUserIsMaintainer)
                        1080.dp
                    else
                        864.dp
                )
                .height(650.dp)
        ) {
            TableHeader(
                currentUserIsMaintainer = currentUserIsMaintainer
            )
            TableContent(
                viewModel = viewModel,
                currentUserIsMaintainer = currentUserIsMaintainer
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun TableHeader(
    currentUserIsMaintainer: Boolean
) {
    val headers = if(currentUserIsMaintainer)
        maintainerHeaders
    else
        developerHeaders
    Row(
        modifier = Modifier
            .height(
                50.dp
            )
            .background(MaterialTheme.colorScheme.surfaceContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyVerticalGrid(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                ),
            columns = GridCells.Fixed(headers.size)
        ) {
            items(
                items = headers
            ) { header ->
                Text(
                    text = stringResource(header),
                    fontFamily = displayFontFamily,
                    color = contentColorFor(
                        MaterialTheme.colorScheme.surfaceContainer
                    )
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun TableContent(
    viewModel: GroupScreenViewModel,
    currentUserIsMaintainer: Boolean
) {
    LazyColumn {
        items(
            items = viewModel.group.value!!.members,
            key = { member -> member.id }
        ) { member ->
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Member(
                    weight = if(currentUserIsMaintainer)
                        1f
                    else
                        1.5f,
                    member = member
                )
                MemberEmail(
                    weight = if(currentUserIsMaintainer)
                        1f
                    else
                        1.8f,
                    member = member
                )
                MemberRole(
                    weight = if(currentUserIsMaintainer)
                        1f
                    else
                        1.7f,
                    member = member
                )
                MemberStatus(
                    member = member
                )
                Actions(
                    weight = if(currentUserIsMaintainer)
                        1f
                    else
                        0.8f,
                    viewModel = viewModel,
                    member = member
                )
            }
            HorizontalDivider()
        }
    }
}

@Composable
@NonRestartableComposable
private fun RowScope.Member(
    weight: Float,
    member: GroupMember
) {
    Row (
        modifier = Modifier
            .weight(weight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Thumbnail(
            size = 45.dp,
            thumbnailData = member.profilePic,
            contentDescription = "Member Profile"
        )
        Text(
            modifier = Modifier
                .padding(
                    start = 15.dp
                ),
            text = member.completeName(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun RowScope.MemberEmail(
    weight: Float,
    member: GroupMember
) {
    Text(
        modifier = Modifier
            .weight(weight),
        text = member.email,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontSize = 14.sp
    )
}

@Composable
@NonRestartableComposable
private fun RowScope.MemberRole(
    weight: Float,
    member: GroupMember
) {
    Column(
        modifier = Modifier
            .weight(weight)
    ) {
        val role = member.role
        Text(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        size = 5.dp
                    )
                )
                .background(MaterialTheme.colorScheme.outlineVariant)
                .padding(
                    horizontal = 4.dp
                ),
            text = role.asText(),
            color = role.color(),
            fontFamily = displayFontFamily,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun RowScope.MemberStatus(
    member: GroupMember
) {
    Column(
        modifier = Modifier
            .weight(1f)
    ) {
        val status = member.status
        Text(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        size = 5.dp
                    )
                )
                .background(status.getBackground())
                .padding(
                    horizontal = 4.dp
                ),
            text = status.asText(),
            color = status.color(),
            fontFamily = displayFontFamily,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
private fun RowScope.Actions(
    weight: Float,
    viewModel: GroupScreenViewModel,
    member: GroupMember
) {
    Row (
        modifier = Modifier
            .weight(weight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = viewModel.group.value!!.checkRolePermissions(member)
        ) {
            Row {
                val expand = remember { mutableStateOf(false) }
                IconButton(
                    enabled = member.joined(),
                    onClick = { expand.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null
                    )
                }
                ChangeMemberRole(
                    expanded = expand,
                    viewModel = viewModel,
                    member = member
                )
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
        }
    }
}

@Composable
@NonRestartableComposable
private fun ChangeMemberRole(
    expanded: MutableState<Boolean>,
    viewModel: GroupScreenViewModel,
    member: GroupMember
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        Role.entries.forEach { role ->
            DropdownMenuItem(
                onClick = {
                    viewModel.changeMemberRole(
                        member = member,
                        role = role,
                        onChange = { expanded.value = false }
                    )
                },
                text = {
                    Text(
                        text = role.asText(),
                        color = role.color()
                    )
                }
            )
        }
    }
}

@Composable
private fun InvitationStatus.getBackground(): Color {
    return when(this) {
        InvitationStatus.PENDING -> {
            if(localUser.theme == Dark || (localUser.theme == Auto && isSystemInDarkTheme()))
                MaterialTheme.colorScheme.outline
            else
                MaterialTheme.colorScheme.outlineVariant
        }
        else -> MaterialTheme.colorScheme.outlineVariant
    }
}