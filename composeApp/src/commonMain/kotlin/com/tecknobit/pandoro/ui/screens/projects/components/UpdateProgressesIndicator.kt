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

private val CHANGE_NOTES_COMPLETED_VALUE = 230.dp

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