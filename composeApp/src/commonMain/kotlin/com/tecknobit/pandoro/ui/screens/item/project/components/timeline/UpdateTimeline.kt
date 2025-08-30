package com.tecknobit.pandoro.ui.screens.item.project.components.timeline

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pushpal.jetlime.EventPosition
import com.pushpal.jetlime.ItemsList
import com.pushpal.jetlime.JetLimeColumn
import com.pushpal.jetlime.JetLimeDefaults
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.shared.data.project.Update
import com.tecknobit.pandoro.ui.screens.shared.data.project.UpdateEvent
import com.tecknobit.pandorocore.enums.UpdateStatus
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.timeline

/**
 * The timeline of the events occurred for the [update]
 *
 * @param update The update from fetch the related events
 */
@Composable
@NonRestartableComposable
fun UpdateTimeline(
    update: Update
) {
    UpdateTimelineContainer(
        itemSpacing = 16.dp,
        update = update,
        content = { position, event ->
            TimelineEvent(
                position = position,
                event = event
            )
        }
    )
}

/**
 * The timeline of the events occurred for the [update] shared with groups
 *
 * @param update The update from fetch the related events
 */
@Composable
@NonRestartableComposable
fun SharedUpdateTimeline(
    update: Update
) {
    UpdateTimelineContainer(
        itemSpacing = 25.dp,
        update = update,
        content = { position, event ->
            SharedTimelineEvent(
                position = position,
                event = event
            )
        }
    )
}

/**
 * The container of the timelines of the [update]
 *
 * @param update The update from fetch the timeline
 * @param content The content to display the timeline
 */
@Composable
// TODO: TO COMMENT
private fun UpdateTimelineContainer(
    itemSpacing: Dp,
    update: Update,
    content: @Composable (EventPosition, UpdateEvent) -> Unit
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
    JetLimeColumn(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        style = if(updatePublished) {
            JetLimeDefaults.columnStyle(
                contentDistance = 20.dp,
                itemSpacing = itemSpacing
            )
        } else {
            JetLimeDefaults.columnStyle(
                contentDistance = 20.dp,
                itemSpacing = itemSpacing,
                pathEffect = PathEffect.dashPathEffect(
                    intervals = floatArrayOf(10f, 10f),
                    phase = 0f,
                )
            )
        },
        itemsList = ItemsList(update.events),
    ) { _, event, position ->
        content(position, event)
    }
}