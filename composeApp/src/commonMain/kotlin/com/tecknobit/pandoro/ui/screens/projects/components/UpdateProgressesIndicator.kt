package com.tecknobit.pandoro.ui.screens.projects.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate

/**
 * **CHANGE_NOTES_COMPLETED_VALUE** -> constant dimension value used as total completion of an update
 */
private val CHANGE_NOTES_COMPLETED_VALUE = 230.dp

/**
 * Indicator used to monitoring the status of the update in terms of tasks completed on the total amount
 * to complete
 *
 * @param modifier The modifier to apply to the indicator
 * @param update The update from fetch the information
 * @param onUpdateCompleted The action to execute when the update has been completed
 */
@Composable
@NonRestartableComposable
fun UpdateProgressesIndicator(
    modifier: Modifier = Modifier,
    update: ProjectUpdate,
    onUpdateCompleted: () -> Unit = {}
) {
    val lineColor = MaterialTheme.colorScheme.primary
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.Bottom
    ) {
        val totalChangeNotes = update.notes.size
        val changeNotesDone = update.notes.filter { note -> note.markedAsDone }.size
        val step = CHANGE_NOTES_COMPLETED_VALUE / totalChangeNotes
        if(totalChangeNotes == changeNotesDone)
            onUpdateCompleted.invoke()
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 5.dp
                ),
            text = "$changeNotesDone/$totalChangeNotes",
            textAlign = TextAlign.End
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            drawLine(
                color = lineColor,
                cap = StrokeCap.Round,
                start = Offset(
                    x = 0f,
                    y = 0f
                ),
                end = Offset(
                    x = (step * changeNotesDone).toPx(),
                    y = 0f
                ),
                strokeWidth = 4.dp.toPx()
            )
        }
    }
}