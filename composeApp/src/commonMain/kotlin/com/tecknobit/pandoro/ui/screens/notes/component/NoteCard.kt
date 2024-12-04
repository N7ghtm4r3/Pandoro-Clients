@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.pandoro.ui.screens.notes.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.pandoro.ui.screens.notes.data.Note

@Composable
@NonRestartableComposable
fun NoteCard(
    note: Note
) {
    Card(
        modifier = Modifier
            .combinedClickable(
                onClick = {
                    // TODO: NAV TO PROJECT
                },
                onLongClick = {
                }
            )
    ) {

    }
}