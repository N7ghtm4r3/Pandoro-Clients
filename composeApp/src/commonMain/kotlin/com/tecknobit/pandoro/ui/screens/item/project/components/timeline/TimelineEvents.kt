@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeExtendedEvent
import com.tecknobit.equinoxcompose.components.TextDivider
import com.tecknobit.equinoxcore.time.TimeFormatter.EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.shared.data.project.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.shared.data.project.UpdateEvent
import com.tecknobit.pandoro.ui.theme.AppTypography
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_ADDED
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_DONE
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_EDITED
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_MOVED_FROM
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_MOVED_TO
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_REMOVED
import com.tecknobit.pandorocore.enums.events.UpdateEventType.CHANGENOTE_UNDONE
import com.tecknobit.pandorocore.enums.events.UpdateEventType.PUBLISHED
import com.tecknobit.pandorocore.enums.events.UpdateEventType.SCHEDULED
import com.tecknobit.pandorocore.enums.events.UpdateEventType.STARTED
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.account_deleted
import pandoro.composeapp.generated.resources.change_note_added_by
import pandoro.composeapp.generated.resources.change_note_added_myself
import pandoro.composeapp.generated.resources.change_note_done_by
import pandoro.composeapp.generated.resources.change_note_done_myself
import pandoro.composeapp.generated.resources.change_note_edited_by
import pandoro.composeapp.generated.resources.change_note_edited_myself
import pandoro.composeapp.generated.resources.change_note_moved_from_by
import pandoro.composeapp.generated.resources.change_note_moved_from_myself
import pandoro.composeapp.generated.resources.change_note_moved_to_by
import pandoro.composeapp.generated.resources.change_note_moved_to_myself
import pandoro.composeapp.generated.resources.change_note_removed_by
import pandoro.composeapp.generated.resources.change_note_removed_myself
import pandoro.composeapp.generated.resources.change_note_todo_by
import pandoro.composeapp.generated.resources.change_note_todo_myself
import pandoro.composeapp.generated.resources.new_content
import pandoro.composeapp.generated.resources.update_published_by
import pandoro.composeapp.generated.resources.update_published_myself
import pandoro.composeapp.generated.resources.update_scheduled_by
import pandoro.composeapp.generated.resources.update_scheduled_myself
import pandoro.composeapp.generated.resources.update_started_by
import pandoro.composeapp.generated.resources.update_started_myself

/**
 * This component displays an [UpdateEvent] content in those project are not shared with groups
 *
 * @param position The position occupied in the timeline
 * @param event The event to display
 *
 * @since 1.2.0
 */
@Composable
fun TimelineEvent(
    position: EventPosition,
    event: UpdateEvent
) {
    TimelineEventImpl(
        position = position,
        contentMaxHeight = 250.dp,
        event = event,
        eventMessage = {
            val eventType = type
            val eventDate = timestamp
            if(eventType == CHANGENOTE_MOVED_TO || eventType == CHANGENOTE_MOVED_FROM) {
                val eventMessage = when(eventType) {
                    CHANGENOTE_MOVED_TO -> Res.string.change_note_moved_to_myself
                    else -> Res.string.change_note_moved_from_myself
                }
                stringResource(
                    resource = eventMessage,
                    eventDate.toDateString(
                        pattern = EUROPEAN_DATE_PATTERN
                    ),
                    eventDate.toDateString(
                        pattern = H24_HOURS_MINUTES_PATTERN
                    ),
                    extraContent!!.asVersionText()
                )
            } else {
                val eventMessage = when(eventType) {
                    SCHEDULED -> Res.string.update_scheduled_myself
                    STARTED -> Res.string.update_started_myself
                    CHANGENOTE_ADDED -> Res.string.change_note_added_myself
                    CHANGENOTE_DONE -> Res.string.change_note_done_myself
                    CHANGENOTE_UNDONE -> Res.string.change_note_todo_myself
                    CHANGENOTE_EDITED -> Res.string.change_note_edited_myself
                    CHANGENOTE_REMOVED -> Res.string.change_note_removed_myself
                    else -> Res.string.update_published_myself
                }
                stringResource(
                    resource = eventMessage,
                    eventDate.toDateString(
                        pattern = EUROPEAN_DATE_PATTERN
                    ),
                    eventDate.toDateString(
                        pattern = H24_HOURS_MINUTES_PATTERN
                    )
                )
            }
        }
    )
}

/**
 * This component displays an [UpdateEvent] content in those project shared with groups
 *
 * @param position The position occupied in the timeline
 * @param event The event to display
 *
 * @since 1.2.0
 */
@Composable
fun SharedTimelineEvent(
    position: EventPosition,
    event: UpdateEvent
) {
    TimelineEventImpl(
        position = position,
        contentMaxHeight = 250.dp,
        event = event,
        headerContent = {
            val accountDeleted = stringResource(Res.string.account_deleted)
            val author = event.author
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Thumbnail(
                    thumbnailData = author?.profilePic,
                    contentDescription = author?.completeName() ?: accountDeleted
                )
                Text(
                    text = author?.completeName() ?: accountDeleted,
                    maxLines = 1,
                    fontFamily = displayFontFamily,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        eventMessage = {
            val eventType = type
            val eventDate = timestamp
            if(eventType == CHANGENOTE_MOVED_TO || eventType == CHANGENOTE_MOVED_FROM) {
                val eventMessage = when(eventType) {
                    CHANGENOTE_MOVED_TO -> Res.string.change_note_moved_to_by
                    else -> Res.string.change_note_moved_from_by
                }
                stringResource(
                    resource = eventMessage,
                    eventDate.toDateString(
                        pattern = EUROPEAN_DATE_PATTERN
                    ),
                    eventDate.toDateString(
                        pattern = H24_HOURS_MINUTES_PATTERN
                    ),
                    extraContent!!.asVersionText()
                )
            } else {
                val eventMessage = when(eventType) {
                    SCHEDULED -> Res.string.update_scheduled_by
                    STARTED -> Res.string.update_started_by
                    CHANGENOTE_ADDED -> Res.string.change_note_added_by
                    CHANGENOTE_DONE -> Res.string.change_note_done_by
                    CHANGENOTE_UNDONE -> Res.string.change_note_todo_by
                    CHANGENOTE_EDITED -> Res.string.change_note_edited_by
                    CHANGENOTE_REMOVED -> Res.string.change_note_removed_by
                    else -> Res.string.update_published_by
                }
                stringResource(
                    resource = eventMessage,
                    eventDate.toDateString(
                        pattern = EUROPEAN_DATE_PATTERN
                    ),
                    eventDate.toDateString(
                        pattern = H24_HOURS_MINUTES_PATTERN
                    )
                )
            }
        }
    )
}

/**
 * This component is the implementation of [TimelineEvent] or [SharedTimelineEvent] where is displayed
 * the content of the [UpdateEvent]
 *
 * @param position The position occupied in the timeline
 * @param contentMaxHeight The max height the content must occupy
 * @param event The event to display
 * @param headerContent Custom header content to display in each event
 * @param eventMessage The message to show to describe the event
 *
 * @since 1.2.0
 */
@Composable
private fun TimelineEventImpl(
    position: EventPosition,
    contentMaxHeight: Dp,
    event: UpdateEvent,
    headerContent: @Composable (() -> Unit)? = null,
    eventMessage: @Composable UpdateEvent.() -> String
) {
    JetLimeExtendedEvent(
        style = JetLimeEventDefaults.eventStyle(
            position = position
        ),
        additionalContentMaxWidth = 85.dp,
        additionalContent = {
            TimelineBadge(
                type = event.type
            )
        }
    ) {
        Column (
            modifier = Modifier
                .heightIn(
                    max = contentMaxHeight
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            headerContent?.invoke()
            Text(
                text = eventMessage(event),
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
                style = AppTypography.labelMedium
            )
            when(event.type) {
                SCHEDULED, STARTED, PUBLISHED -> {}
                CHANGENOTE_EDITED -> {
                    ChangeNoteContent(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        noteContent = event.extraContent
                    )
                    TextDivider(
                        text = stringResource(Res.string.new_content),
                        textStyle = AppTypography.labelLarge
                    )
                    ChangeNoteContent(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        noteContent = event.noteContent
                    )
                }
                else -> {
                    ChangeNoteContent(
                        noteContent = event.noteContent
                    )
                }
            }
        }
    }
}

/**
 * This component allows to display the content of a change note attached to an event
 *
 * @param modifier The modifier to apply to the component
 * @param noteContent The content of the change note
 *
 * @since 1.2.0
 */
@Composable
private fun ChangeNoteContent(
    modifier: Modifier = Modifier,
    noteContent: String?
) {
    Card (
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
                .verticalScroll(rememberScrollState()),
            text = noteContent!!,
            style = AppTypography.bodyMedium,
            overflow = TextOverflow.Ellipsis
        )
    }
}