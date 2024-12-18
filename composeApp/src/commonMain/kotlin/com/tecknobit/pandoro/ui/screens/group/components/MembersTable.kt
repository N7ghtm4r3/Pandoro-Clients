package com.tecknobit.pandoro.ui.screens.group.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.color
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.actions
import pandoro.composeapp.generated.resources.email
import pandoro.composeapp.generated.resources.member
import pandoro.composeapp.generated.resources.role
import pandoro.composeapp.generated.resources.status

private val headers = listOf(
    Res.string.member,
    Res.string.email,
    Res.string.role,
    Res.string.status,
    Res.string.actions
)

@Composable
@NonRestartableComposable
fun MembersTable(
    viewModel: GroupScreenViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(
                    max = 1080.dp
                )
                .height(600.dp)
        ) {
            TableHeader()
            TableContent(
                viewModel = viewModel
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun TableHeader() {
    Row(
        modifier = Modifier
            .height(
                50.dp
            )
            .background(MaterialTheme.colorScheme.surfaceContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyVerticalGrid(
            modifier = Modifier,
            columns = GridCells.Fixed(headers.size)
        ) {
            items(
                items = headers
            ) { header ->
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = stringResource(header),
                    textAlign = TextAlign.Center,
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
    viewModel: GroupScreenViewModel
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
                Row (
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
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
                        fontSize = 14.sp
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = member.email,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                val role = member.role
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = role.asText(),
                    color = role.color(),
                    fontFamily = displayFontFamily,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                val status = member.status
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = status.asText(),
                    color = status.color(),
                    fontSize = 14.sp,
                    fontFamily = displayFontFamily,
                    textAlign = TextAlign.Center
                )
                Row (
                    modifier = Modifier
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChangeCircle,
                            contentDescription = null
                        )
                    }
                    IconButton(
                        onClick = {

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
            HorizontalDivider()
        }
    }
}