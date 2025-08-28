@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.helpers.PROJECT_SCREEN
import com.tecknobit.pandoro.helpers.navigator
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The projects icons of the projects shared with the [group]
 *
 * @param modifier The modifier to apply to the component
 * @param group The group from fetch the projects
 */
@Composable
fun ProjectIcons(
    modifier: Modifier = Modifier,
    group: Group
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Box (
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    size = 50.dp
                )
            )
            .clickable(
                onClick = {
                    scope.launch { modalBottomSheetState.show() }
                }
            )
    ) {
        group.projects.forEachIndexed { index, project ->
            Thumbnail(
                modifier = Modifier
                    .padding(
                        start = 15.dp * index
                    ),
                size = 30.dp,
                thumbnailData = project.icon,
                contentDescription = "Project icon"
            )
        }
    }
    ProjectsListExpanded(
        state = modalBottomSheetState,
        scope = scope,
        group = group
    )
}

/**
 * The expanded list of the project shared with the [group]
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param group The group from fetch the projects
 */
@Composable
@NonRestartableComposable
private fun ProjectsListExpanded(
    state: SheetState,
    scope: CoroutineScope,
    group: Group
) {
    ProjectsListExpanded(
        state = state,
        scope = scope,
        projects = group.projects,
        trailingContent = { project ->
            IconButton(
                onClick = {
                    navigator.navigate("$PROJECT_SCREEN/${project.id}")
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

/**
 * The projects icons list
 *
 * @param projects The list of the projects
 * @param onClick The action to execute when the list is clicked
 */
@Composable
@NonRestartableComposable
fun ProjectIcons(
    projects: List<Project>,
    onClick: () -> Unit
) {
    Box (
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    size = 50.dp
                )
            )
            .clickable(
                onClick = onClick
            )
    ) {
        projects.forEachIndexed { index, project ->
            Thumbnail(
                modifier = Modifier
                    .padding(
                        start = 15.dp * index
                    ),
                size = 30.dp,
                thumbnailData = project.icon,
                contentDescription = "Project Icon"
            )
        }
    }
}

/**
 * List of the candidate projects to share with the group
 *
 * @param extraOnDismissAction The action to execute when [ModalBottomSheet] dismissed
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param projects The list of the projects
 * @param trailingContent The content at the end of the layout
 */
@Composable
@NonRestartableComposable
fun GroupProjectsCandidate(
    extraOnDismissAction: (() -> Unit)? = null,
    state: SheetState,
    scope: CoroutineScope,
    projects: List<Project>,
    trailingContent: @Composable (Project) -> Unit
) {
    ProjectsListExpanded(
        state = state,
        scope = scope,
        extraOnDismissAction = extraOnDismissAction,
        projects = projects,
        trailingContent = trailingContent
    )
}

/**
 * Expanded list of the projects to display each project details
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param projects The list of the projects
 * @param extraOnDismissAction The action to execute when [ModalBottomSheet] dismissed
 * @param trailingContent The content at the end of the layout
 */
@Composable
private fun ProjectsListExpanded(
    state: SheetState,
    scope: CoroutineScope,
    projects: List<Project>,
    extraOnDismissAction: (() -> Unit)? = null,
    trailingContent: @Composable (Project) -> Unit
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
            LazyColumn {
                items(
                    items = projects,
                    key = { project -> project.id }
                ) { project ->
                    ProjectListItem(
                        project = project,
                        trailingContent = trailingContent
                    )
                }
            }
        }
    }
}

/**
 * Project item details
 *
 * @param project The project to display
 * @param trailingContent The content at the end of the layout
 */
@Composable
@NonRestartableComposable
private fun ProjectListItem(
    project: Project,
    trailingContent: @Composable (Project) -> Unit
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            Thumbnail(
                size = 50.dp,
                thumbnailData = project.icon,
                contentDescription = "Project icon"
            )
        },
        overlineContent = {
            Text(
                text = project.version.asVersionText()
            )
        },
        headlineContent = {
            Text(
                text = project.name
            )
        },
        trailingContent = { trailingContent.invoke(project) }
    )
    HorizontalDivider()
}

