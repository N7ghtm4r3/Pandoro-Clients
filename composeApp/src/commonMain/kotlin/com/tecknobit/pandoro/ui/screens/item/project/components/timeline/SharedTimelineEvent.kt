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
import pandoro.composeapp.generated.resources.change_note_done_by
import pandoro.composeapp.generated.resources.change_note_edited_by
import pandoro.composeapp.generated.resources.change_note_moved_from_by
import pandoro.composeapp.generated.resources.change_note_moved_to_by
import pandoro.composeapp.generated.resources.change_note_removed_by
import pandoro.composeapp.generated.resources.change_note_todo_by
import pandoro.composeapp.generated.resources.new_content
import pandoro.composeapp.generated.resources.update_published_by
import pandoro.composeapp.generated.resources.update_scheduled_by
import pandoro.composeapp.generated.resources.update_started_by

// TODO: TO COMMENT 1.2.0

@Composable
fun SharedTimelineEvent(
    position: EventPosition,
    event: UpdateEvent
) {
    val accountDeleted = stringResource(Res.string.account_deleted)
    val author = event.author
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
                    max = 300.dp
                ),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
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
            Text(
                text = event.resolveText(),
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

@Composable
private fun UpdateEvent.resolveText() : String {
    val eventType = type
    val eventDate = timestamp
    return if(eventType == CHANGENOTE_MOVED_TO || eventType == CHANGENOTE_MOVED_FROM) {
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