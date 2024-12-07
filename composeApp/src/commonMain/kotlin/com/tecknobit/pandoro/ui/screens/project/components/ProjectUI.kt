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
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val VERSION_PREFIX = "v"

@Composable
@NonRestartableComposable
fun ProjectIcons(
    group: Group
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Box (
        modifier = Modifier
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
        modalBottomSheetState = modalBottomSheetState,
        scope = scope,
        group = group
    )
}

@Composable
@NonRestartableComposable
private fun ProjectsListExpanded(
    modalBottomSheetState: SheetState,
    scope: CoroutineScope,
    group: Group
) {
    if(modalBottomSheetState.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { modalBottomSheetState.hide() }
            }
        ) {
            LazyColumn {
                items(
                    items = group.projects,
                    key = { project -> project.id }
                ) { project ->
                    ProjectListItem(
                        project = project
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun ProjectListItem(
    project: Project
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
        trailingContent = {
            IconButton(
                onClick = {
                    // TODO: NAV TO PROJECT
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null
                )
            }
        }
    )
    HorizontalDivider()
}

fun String.asVersionText() : String {
    return if(this.startsWith(VERSION_PREFIX))
        return this
    else
        "$VERSION_PREFIX$this"
}