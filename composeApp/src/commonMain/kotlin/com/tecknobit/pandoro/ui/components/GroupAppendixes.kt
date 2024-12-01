@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.group.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.logo
import pandoro.composeapp.generated.resources.members_number
import pandoro.composeapp.generated.resources.project_groups_title

private const val LIMIT_GROUPS_DISPLAYED = 5

@Composable
@NonRestartableComposable
fun GroupIcons(
    project: Project,
) {
    val groups = project.groups
    val expandGroups = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .clip(
                RoundedCornerShape(
                    size = 50.dp
                )
            )
            .clickable { expandGroups.value = true }
    ) {
        groups.forEachIndexed { index, group ->
            if(index >= LIMIT_GROUPS_DISPLAYED)
                return@forEachIndexed
            GroupLogo(
                modifier = Modifier
                    .padding(
                        start = (15 * index).dp
                    )
                    .size(30.dp),
                group = group
            )
        }
    }
    GroupExpandedList(
        expanded = expandGroups,
        project = project,
        groups = groups
    )
}

@Composable
@NonRestartableComposable
fun GroupExpandedList(
    expanded: MutableState<Boolean>,
    project: Project? = null,
    groups: List<Group>,
) {
    if(expanded.value) {
        ModalBottomSheet(
            onDismissRequest = { expanded.value = false }
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
            LazyColumn {
                items(
                    items = groups,
                    key = { group -> group.id }
                ) { group ->
                    GroupListItem(
                        group = group
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun GroupListItem(
    group: Group
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            GroupLogo(
                modifier = Modifier
                    .size(50.dp),
                group = group
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
        trailingContent = {
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
    HorizontalDivider()
}

@Composable
@NonRestartableComposable
private fun GroupLogo(
    modifier: Modifier,
    group: Group
) {
    AsyncImage(
        modifier = modifier
            .clip(CircleShape),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(group.logo)
            .crossfade(true)
            .crossfade(500)
            .build(),
        // TODO: TO SET
        //imageLoader = imageLoader,
        contentDescription = "Group logo",
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo)
    )
}