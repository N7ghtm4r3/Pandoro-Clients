package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.tecknobit.pandoro.helpers.TimeFormatter.DATE_PATTERN
import com.tecknobit.pandoro.helpers.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsTimeString
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.account_deleted
import pandoro.composeapp.generated.resources.timeline
import pandoro.composeapp.generated.resources.update_published_by
import pandoro.composeapp.generated.resources.update_published_myself
import pandoro.composeapp.generated.resources.update_scheduled_by
import pandoro.composeapp.generated.resources.update_scheduled_myself
import pandoro.composeapp.generated.resources.update_started_by
import pandoro.composeapp.generated.resources.update_started_myself

/**
 * The timeline of the events occurred for the [update]
 *
 * @param update The update from fetch the related events
 */
@Composable
@NonRestartableComposable
fun UpdateTimeline(
    update: ProjectUpdate
) {
    UpdateTimelineContainer(
        update = update,
        content = { index, date, position, updatePublished ->
            when(index) {
                0 -> {
                    TimelineEvent(
                        position = position,
                        eventMessage = Res.string.update_scheduled_myself,
                        eventDate = date
                    )
                }
                1 -> {
                    if(update.status == IN_DEVELOPMENT || updatePublished) {
                        TimelineEvent(
                            position = position,
                            eventMessage = Res.string.update_started_myself,
                            eventDate = date
                        )
                    }
                }
                else -> {
                    if(updatePublished) {
                        TimelineEvent(
                            position = position,
                            eventMessage = Res.string.update_published_myself,
                            eventDate = date
                        )
                    }
                }
            }
        }
    )
}

/**
 * The timeline event to display its details
 *
 * @param position The position in the timeline
 * @param eventMessage The message of the event
 * @param eventDate The date of the event
 */
@Composable
@NonRestartableComposable
private fun TimelineEvent(
    position: EventPosition,
    eventMessage: StringResource,
    eventDate: Long
) {
    JetLimeEvent(
        style = JetLimeEventDefaults.eventStyle(
            position = position
        )
    ) {
        EventDateInfo(
            modifier = Modifier
                .heightIn(
                    min = 55.dp
                ),
            eventMessage = eventMessage,
            eventDate = eventDate
        )
    }
}

/**
 * The timeline of the events occurred for the [update] shared with groups
 *
 * @param update The update from fetch the related events
 */
@Composable
@NonRestartableComposable
fun SharedUpdateTimeline(
    update: ProjectUpdate
) {
    UpdateTimelineContainer(
        update = update,
        content = { index, date, position, updatePublished ->
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
                            eventAuthor = update.startedBy,
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
                            eventAuthor = update.publishedBy,
                            contentDescription = "User who published the update profile pic",
                            eventMessage = Res.string.update_published_by,
                            eventDate = date
                        )
                    }
                }
            }
        }
    )
}

/**
 * The timeline of a shared event to display its details
 *
 * @param position The position in the timeline
 * @param eventAuthor The author of the event such creation, starting or publishing
 * @param contentDescription The description of the content displayed by the event
 * @param eventMessage The message of the event
 * @param eventDate The date of the event
 */
@Composable
@NonRestartableComposable
private fun SharedTimelineEvent(
    position: EventPosition,
    eventAuthor: GroupMember?,
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
                    thumbnailData = eventAuthor?.profilePic,
                    contentDescription = contentDescription
                )
                Text(
                    text = if(eventAuthor == null)
                        stringResource(Res.string.account_deleted)
                    else
                        eventAuthor.completeName(),
                    maxLines = 1,
                    fontFamily = displayFontFamily,
                    overflow = TextOverflow.Ellipsis
                )
            }
            EventDateInfo(
                eventMessage = eventMessage,
                eventDate = eventDate
            )
        }
    }
}

/**
 * The info about the date of the event
 *
 * @param modifier The modifier to apply to the component
 * @param eventMessage The message of the event
 * @param eventDate The date of the event
 */
@Composable
@NonRestartableComposable
private fun EventDateInfo(
    modifier: Modifier = Modifier,
    eventMessage: StringResource,
    eventDate: Long
) {
    Text(
        modifier = modifier,
        text = stringResource(
            resource = eventMessage,
            eventDate.formatAsTimeString(
                pattern = DATE_PATTERN
            ),
            eventDate.formatAsTimeString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        ),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        fontSize = 12.sp
    )
}

/**
 * The container of the timelines of the [update]
 *
 * @param update The update from fetch the timeline
 * @param content The content to display the timeline
 */
@Composable
@NonRestartableComposable
private fun UpdateTimelineContainer(
    update: ProjectUpdate,
    content: @Composable (Int, Long, EventPosition, Boolean)-> Unit
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
        content(index, date, position, updatePublished)
    }
}