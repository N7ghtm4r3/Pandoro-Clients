package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.groups.data.Group

@Composable
@NonRestartableComposable
fun ProjectIcons(
    group: Group
) {
    Box {
        group.projects.forEachIndexed { index, project ->
            Thumbnail(
                modifier = Modifier
                    .padding(
                        start = 15.dp * index
                    ),
                size = 30.dp,
                thumbnailData = project.icon,
                contentDescription = "Project icon"
            )
        }
    }
}