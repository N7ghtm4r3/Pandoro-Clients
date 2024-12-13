@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsTimeString
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.at
import pandoro.composeapp.generated.resources.timeline
import pandoro.composeapp.generated.resources.update_published_by
import pandoro.composeapp.generated.resources.update_scheduled_by
import pandoro.composeapp.generated.resources.update_started_by

@Composable
@NonRestartableComposable
fun UpdateTimeline(
    update: ProjectUpdate
) {
//    Text(
//        modifier = Modifier
//            .padding(
//                start = 16.dp
//            ),
//        text = stringResource(Res.string.timeline),
//        fontFamily = displayFontFamily,
//        fontSize = 20.sp
//    )
//    val items = remember {
//        listOf(
//            update.creationDate.formatAsTimeString(),
//            update.startDate.formatAsTimeString(),
//            update.publishDate.formatAsTimeString()
//        )
//    }
//    JetLimeColumn(
//        modifier = Modifier
//            .padding(
//                top = 10.dp,
//                start = 16.dp
//            ),
//        style = if(noteCompleted)
//            JetLimeDefaults.columnStyle()
//        else {
//            JetLimeDefaults.columnStyle(
//                pathEffect = PathEffect.dashPathEffect(
//                    intervals = FloatArray(items.size) { 10f },
//                    phase = 0f,
//                )
//            )
//        },
//        itemsList = ItemsList(items),
//    ) { _, date, position ->
//        val startPosition = position.isNotEnd()
//        JetLimeEvent(
//            style = JetLimeEventDefaults.eventStyle(
//                position = position,
//                pointType = if(!startPosition) {
//                    EventPointType.custom(
//                        icon = rememberVectorPainter(
//                            image = if(noteCompleted)
//                                Icons.Default.CheckCircle
//                            else
//                                ClockLoader20
//                        ),
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                } else
//                    EventPointType.Default
//            )
//        ) {
//            Column (
//                modifier = Modifier
//                    .height(60.dp)
//            ) {
//                Text(
//                    text = if(noteCompleted || startPosition)
//                        date
//                    else
//                        stringResource(Res.string.not_available),
//                    fontSize = 12.sp
//                )
//                Text(
//                    text = if(startPosition)
//                        stringResource(Res.string.note_created)
//                    else {
//                        if(noteCompleted) {
//                            val completionDays = note.completionDays()
//                            pluralStringResource(
//                                resource = Res.plurals.note_completed_in,
//                                quantity = completionDays,
//                                completionDays
//                            )
//                        } else
//                            date
//                    },
//                    fontFamily = displayFontFamily
//                )
//            }
//        }
//    }
}

@Composable
@NonRestartableComposable
fun SharedUpdateTimeline(
    update: ProjectUpdate
) {
    val updatePublished = update.status == UpdateStatus.PUBLISHED
    Text(
        modifier = Modifier
            .padding(
                start = 16.dp
            ),
        text = stringResource(Res.string.timeline),
        fontFamily = displayFontFamily,
        fontSize = 20.sp
    )
    val items = remember {
        listOf(
            update.creationDate,
            update.startDate,
            update.publishDate
        )
    }
    JetLimeColumn(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        style = if(updatePublished)
            JetLimeDefaults.columnStyle()
        else {
            JetLimeDefaults.columnStyle(
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 0f,
                )
            )
        },
        itemsList = ItemsList(items),
    ) { index, date, position ->
        when(index) {
            0 -> {
                SharedTimelineEvent(
                    position = position,
                    eventAuthor = update.author,
                    contentDescription = "User who scheduled the update profile pic",
                    eventMessage = Res.string.update_scheduled_by,
                    eventDate = date
                )
            }
            1 -> {
                if(update.status == IN_DEVELOPMENT || updatePublished) {
                    SharedTimelineEvent(
                        position = position,
                        eventAuthor = update.startedBy!!,
                        contentDescription = "User who started the update profile pic",
                        eventMessage = Res.string.update_started_by,
                        eventDate = date
                    )
                }
            }
            else -> {
                if(updatePublished) {
                    SharedTimelineEvent(
                        position = position,
                        eventAuthor = update.publishedBy!!,
                        contentDescription = "User who published the update profile pic",
                        eventMessage = Res.string.update_published_by,
                        eventDate = date
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun SharedTimelineEvent(
    position: EventPosition,
    eventAuthor: GroupMember,
    contentDescription: String,
    eventMessage: StringResource,
    eventDate: Long
) {
    JetLimeEvent(
        style = JetLimeEventDefaults.eventStyle(
            position = position
        )
    ) {
        Column {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Thumbnail(
                    thumbnailData = eventAuthor.profilePic,
                    contentDescription = contentDescription
                )
                Text(
                    text = eventAuthor.completeName(),
                    maxLines = 1,
                    fontFamily = displayFontFamily,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Text(
                text = stringResource(
                    resource = eventMessage,
                    eventDate.formatAsTimeString(
                        pattern = "dd/MM/yyyy"
                    )
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
            Text(
                text = stringResource(
                    resource = Res.string.at,
                    eventDate.formatAsTimeString(
                        pattern = "HH:mm"
                    )
                ),
                maxLines = 1,
                fontSize = 12.sp
            )
        }
    }
}