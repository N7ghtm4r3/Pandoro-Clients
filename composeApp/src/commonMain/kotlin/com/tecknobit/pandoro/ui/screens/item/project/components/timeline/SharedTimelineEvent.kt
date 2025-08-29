package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.JetLimeEvent
import com.pushpal.jetlime.JetLimeEventDefaults
import com.tecknobit.equinoxcore.time.TimeFormatter.EUROPEAN_DATE_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.H24_HOURS_MINUTES_PATTERN
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.components.Thumbnail
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
import pandoro.composeapp.generated.resources.account_deleted
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
                text = event.type.resolveText(
                    eventDate = event.timestamp
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun UpdateEventType.resolveText(
    eventDate: Long
) : String {
    return when(this) {
        SCHEDULED -> stringResource(
            resource = Res.string.update_scheduled_by,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
        STARTED -> stringResource(
            resource = Res.string.update_started_by,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
        CHANGENOTE_ADDED -> TODO()
        CHANGENOTE_DONE -> TODO()
        CHANGENOTE_UNDONE -> TODO()
        CHANGENOTE_EDITED -> TODO()
        CHANGENOTE_MOVED_TO -> TODO()
        CHANGENOTE_MOVED_FROM -> TODO()
        CHANGENOTE_REMOVED -> TODO()
        PUBLISHED -> stringResource(
            resource = Res.string.update_published_by,
            eventDate.toDateString(
                pattern = EUROPEAN_DATE_PATTERN
            ),
            eventDate.toDateString(
                pattern = H24_HOURS_MINUTES_PATTERN
            )
        )
    }
}