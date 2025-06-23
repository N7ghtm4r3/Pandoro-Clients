package com.tecknobit.pandoro.ui.screens.profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.profile.data.Changelog
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.theme.green
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.decline
import pandoro.composeapp.generated.resources.join

/**
 * The card to display the changelog details
 *
 * @param viewModel The support viewmodel for the screen
 * @param changelog The changelog to display
 */
@Composable
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
        overlineContent = if(changelog.isInviteToGroupType()) {
            {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Button(
                        modifier = Modifier
                            .height(35.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = green()
                        ),
                        shape = RoundedCornerShape(
                            size = 5.dp
                        ),
                        onClick = {
                            viewModel.acceptInvite(
                                changelog = changelog
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.join),
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        modifier = Modifier
                            .height(35.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(
                            size = 5.dp
                        ),
                        onClick = {
                            viewModel.declineInvite(
                                changelog = changelog
                            )
                        }
                    ) {
                        Text(
                            text = stringResource(Res.string.decline),
                            fontSize = 12.sp
                        )
                    }
                }
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