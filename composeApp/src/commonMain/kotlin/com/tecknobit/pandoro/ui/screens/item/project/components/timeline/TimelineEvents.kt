@file:OptIn(ExperimentalComposeApi::class, ExperimentalComposeUiApi::class)

package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.JetLimeEventDefaults
import com.pushpal.jetlime.JetLimeExtendedEvent
import com.tecknobit.equinoxcore.time.TimeFormatter.EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.pandoro.ui.screens.shared.data.project.UpdateEvent
import com.tecknobit.pandorocore.enums.events.UpdateEventType
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
import pandoro.composeapp.generated.resources.update_published_myself
import pandoro.composeapp.generated.resources.update_scheduled_myself
import pandoro.composeapp.generated.resources.update_started_myself

// TODO: TO COMMENT 1.2.0

@Composable
fun TimelineEvent(
    position: EventPosition,
    event: UpdateEvent
) {
    JetLimeExtendedEvent (
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
        Text(
            modifier = Modifier
                .heightIn(
                    min = 55.dp
                ),
            text = event.type.resolveText(
                eventDate = event.timestamp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun UpdateEventType.resolveText(
    eventDate: Long
) : String {
    return when(this) {
        SCHEDULED -> stringResource(
            resource = Res.string.update_scheduled_myself,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
        STARTED -> stringResource(
            resource = Res.string.update_started_myself,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
        CHANGENOTE_ADDED -> "TODO()"
        CHANGENOTE_DONE -> "TODO()"
        CHANGENOTE_UNDONE -> "TODO()"
        CHANGENOTE_EDITED -> "TODO()"
        CHANGENOTE_MOVED_TO -> "TODO()"
        CHANGENOTE_MOVED_FROM -> "TODO()"
        CHANGENOTE_REMOVED -> "TODO()"
        PUBLISHED -> stringResource(
            resource = Res.string.update_published_myself,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
    }
}