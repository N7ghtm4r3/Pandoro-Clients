@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.group.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.members_number
import pandoro.composeapp.generated.resources.project_groups_title
import pandoro.composeapp.generated.resources.share_the_project

private const val LIMIT_GROUPS_DISPLAYED = 5

@Composable
@NonRestartableComposable
fun GroupIcons(
    modifier: Modifier = Modifier,
    project: Project,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val groups = project.groups
    GroupIcons(
        modifier = modifier,
        groups = groups,
        onClick = {
            scope.launch {
                modalBottomSheetState.show()
            }
        }
    )
    GroupExpandedList(
        state = modalBottomSheetState,
        scope = scope,
        project = project,
        groups = groups
    )
}

@Composable
@NonRestartableComposable
fun GroupExpandedList(
    state: SheetState,
    scope: CoroutineScope,
    project: Project? = null,
    groups: List<Group>,
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            project?.let { project ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 5.dp
                        ),
                    text = stringResource(Res.string.project_groups_title, project.name),
                    textAlign = TextAlign.Center,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider()
            }
            GroupsList(
                groups = groups,
                trailingContent = { group ->
                    IconButton(
                        onClick = {
                            // TODO: NAV TO GROUP
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}

@Composable
@NonRestartableComposable
fun GroupIcons(
    modifier: Modifier = Modifier,
    groups: List<Group>,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    size = 50.dp
                )
            )
            .clickable(
                onClick = onClick
            )
    ) {
        groups.forEachIndexed { index, group ->
            if(index >= LIMIT_GROUPS_DISPLAYED)
                return@forEachIndexed
            Thumbnail(
                modifier = Modifier
                    .padding(
                        start = (15 * index).dp
                    ),
                size = 30.dp,
                thumbnailData = group.logo,
                contentDescription = "Group logo"
            )
        }
    }
}

@Composable
@NonRestartableComposable
fun GroupsProjectCandidate(
    state: SheetState,
    scope: CoroutineScope,
    groups: List<Group>,
    trailingContent: @Composable (Group) -> Unit
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 5.dp
                    ),
                text = stringResource(Res.string.share_the_project),
                textAlign = TextAlign.Center,
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            HorizontalDivider()
            GroupsList(
                groups = groups,
                trailingContent = trailingContent
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun GroupsList(
    groups: List<Group>,
    trailingContent: @Composable (Group) -> Unit
) {
    LazyColumn {
        items(
            items = groups,
            key = { group -> group.id }
        ) { group ->
            GroupListItem(
                group = group,
                trailingContent = trailingContent
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun GroupListItem(
    group: Group,
    trailingContent: @Composable (Group) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            Thumbnail(
                size = 50.dp,
                thumbnailData = group.logo,
                contentDescription = "Group logo"
            )
        },
        overlineContent = {
            val members = group.members.size
            Text(
                text = pluralStringResource(
                    resource = Res.plurals.members_number,
                    quantity = members,
                    members
                )
            )
        },
        headlineContent = {
            Text(
                text = group.name
            )
        },
        trailingContent = { trailingContent.invoke(group) }
    )
    HorizontalDivider()
}