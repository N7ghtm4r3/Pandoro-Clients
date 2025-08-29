@file:OptIn(ExperimentalComposeUiApi::class)

package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

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

// TODO: TO COMMENT 1.2.0
@Composable
fun TimelineBadge(
    type: UpdateEventType
) {
    val badgeColor = type.resolveColor()
    BadgeText(
        modifier = Modifier
            .fillMaxWidth(),
        badgeText = type.resolveText(),
        badgeColor = badgeColor,
        textColor = getContrastColor(
            backgroundColor = badgeColor
        ),
        badgeTextStyle = AppTypography.labelSmall,
        shape = RoundedCornerShape(
            size = 6.dp
        )
    )
}

fun UpdateEventType.resolveText() : String {
    return when(this) {
        SCHEDULED, STARTED, PUBLISHED -> name.lowercase()
        CHANGENOTE_ADDED -> "added"
        CHANGENOTE_DONE -> "done"
        CHANGENOTE_UNDONE -> "to-do"
        CHANGENOTE_EDITED -> "edited"
        CHANGENOTE_MOVED_TO -> "moved to"
        CHANGENOTE_MOVED_FROM -> "moved from"
        CHANGENOTE_REMOVED -> "removed"
    }
}

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