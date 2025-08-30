@file:OptIn(ExperimentalComposeUiApi::class)

package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.BadgeText
import com.tecknobit.equinoxcompose.components.getContrastColor
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.pandoro.ui.theme.AppTypography
import com.tecknobit.pandoro.ui.theme.changeNoteAddedColor
import com.tecknobit.pandoro.ui.theme.changeNoteDoneColor
import com.tecknobit.pandoro.ui.theme.changeNoteEditedColor
import com.tecknobit.pandoro.ui.theme.changeNoteMovedFromColor
import com.tecknobit.pandoro.ui.theme.changeNoteMovedToColor
import com.tecknobit.pandoro.ui.theme.changeNoteRemovedColor
import com.tecknobit.pandoro.ui.theme.changeNoteTodoColor
import com.tecknobit.pandoro.ui.theme.green
import com.tecknobit.pandoro.ui.theme.yellow
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

/**
 * `CHANGENOTE_ADDED_BADGE_TEXT` the text to display when the event is [CHANGENOTE_ADDED]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_ADDED_BADGE_TEXT = "added"

/**
 * `CHANGENOTE_DONE_BADGE_TEXT` the text to display when the event is [CHANGENOTE_DONE]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_DONE_BADGE_TEXT = "done"

/**
 * `CHANGENOTE_UNDONE_BADGE_TEXT` the text to display when the event is [CHANGENOTE_UNDONE]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_UNDONE_BADGE_TEXT = "to-do"

/**
 * `CHANGENOTE_EDITED_BADGE_TEXT` the text to display when the event is [CHANGENOTE_EDITED]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_EDITED_BADGE_TEXT = "edited"

/**
 * `CHANGENOTE_MOVED_TO_BADGE_TEXT` the text to display when the event is [CHANGENOTE_MOVED_TO]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_MOVED_TO_BADGE_TEXT = "moved to"

/**
 * `CHANGENOTE_MOVED_FROM_BADGE_TEXT` the text to display when the event is [CHANGENOTE_MOVED_FROM]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_MOVED_FROM_BADGE_TEXT = "moved from"

/**
 * `CHANGENOTE_REMOVED_BADGE_TEXT` the text to display when the event is [CHANGENOTE_REMOVED]
 *
 * @since 1.2.0
 */
private const val CHANGENOTE_REMOVED_BADGE_TEXT = "removed"

/**
 * Custom [BadgeText] used to display the event type
 *
 * @param type The type of the event
 *
 * @since 1.2.0
 */
@Composable
fun TimelineBadge(
    type: UpdateEventType
) {
    val badgeColor = type.resolveColor()
    BadgeText(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(
            size = 6.dp
        ),
        padding = PaddingValues(
            vertical = 5.dp,
            horizontal = 2.dp
        ),
        elevation = 1.dp,
        badgeTextStyle = AppTypography.labelMedium,
        textColor = getContrastColor(
            backgroundColor = badgeColor
        ),
        badgeText = type.resolveText(),
        badgeColor = badgeColor
    )
}

/**
 * Method used to get the proper text based on the [UpdateEventType]
 *
 * @return the proper text to represent the event type as [String]
 *
 * @since 1.2.0
 */
@Returner
fun UpdateEventType.resolveText() : String {
    return when(this) {
        SCHEDULED, STARTED, PUBLISHED -> name.lowercase()
        CHANGENOTE_ADDED -> CHANGENOTE_ADDED_BADGE_TEXT
        CHANGENOTE_DONE -> CHANGENOTE_DONE_BADGE_TEXT
        CHANGENOTE_UNDONE -> CHANGENOTE_UNDONE_BADGE_TEXT
        CHANGENOTE_EDITED -> CHANGENOTE_EDITED_BADGE_TEXT
        CHANGENOTE_MOVED_TO -> CHANGENOTE_MOVED_TO_BADGE_TEXT
        CHANGENOTE_MOVED_FROM -> CHANGENOTE_MOVED_FROM_BADGE_TEXT
        CHANGENOTE_REMOVED -> CHANGENOTE_REMOVED_BADGE_TEXT
    }
}

/**
 * Method used to get the proper color based on the [UpdateEventType]
 *
 * @return the proper color to represent the event type as [Color]
 *
 * @since 1.2.0
 */
@Returner
@Composable
fun UpdateEventType.resolveColor(): Color {
    return when(this) {
        SCHEDULED -> MaterialTheme.colorScheme.error
        STARTED -> yellow()
        CHANGENOTE_ADDED -> changeNoteAddedColor()
        CHANGENOTE_DONE -> changeNoteDoneColor()
        CHANGENOTE_UNDONE -> changeNoteTodoColor()
        CHANGENOTE_EDITED -> changeNoteEditedColor()
        CHANGENOTE_MOVED_TO -> changeNoteMovedToColor()
        CHANGENOTE_MOVED_FROM -> changeNoteMovedFromColor()
        CHANGENOTE_REMOVED -> changeNoteRemovedColor()
        PUBLISHED -> green()
    }
}