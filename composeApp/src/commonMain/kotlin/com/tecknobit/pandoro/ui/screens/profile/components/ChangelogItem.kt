package com.tecknobit.pandoro.ui.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.profile.data.Changelog
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel

@Composable
@NonRestartableComposable
fun ChangelogItem(
    viewModel: ProfileScreenViewModel,
    changelog: Changelog
) {
    val changelogRead = changelog.read
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = if(changelogRead)
                Color.Transparent
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .clickable(
                enabled = !changelogRead
            ) {
                viewModel.readChangelog(
                    changelog = changelog
                )
            },
        leadingContent = if(changelog.hasItemsData()) {
            {
                Thumbnail(
                    size = 45.dp,
                    thumbnailData = changelog.thumbnail(),
                    contentDescription = "ChangelogItem thumbnail"
                )
            }
        } else
            null,
        headlineContent = {
            Text(
                text = changelog.getTitle(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = changelog.getContent(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            IconButton(
                onClick = {
                    viewModel.deleteChangelog(
                        changelog = changelog
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    )
    HorizontalDivider()
}