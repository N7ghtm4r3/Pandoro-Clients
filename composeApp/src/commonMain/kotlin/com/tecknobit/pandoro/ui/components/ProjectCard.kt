package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.data.Project

const val VERSION_PREFIX = "v"

@Composable
@NonRestartableComposable
fun InDevelopmentProjectCard(
    inDevelopmentProject: InDevelopmentProject
) {
    val project = inDevelopmentProject.project
    val update = inDevelopmentProject.update
    Card(
        modifier = Modifier
            .size(
                width = 250.dp,
                height = 150.dp
            ),
        onClick = {
            // TODO: TO SHOW THE NOTE
        }
    ) {
        Column (
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            Text(
                text = project.name,
                fontFamily = displayFontFamily,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row (
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = update.targetVersion.asVersionText()
                )
                Icon(
                    imageVector = Icons.Default.Upgrade,
                    contentDescription = null
                )
            }
            UpdateProgressesIndicator(
                modifier = Modifier
                    .fillMaxHeight(),
                update = update
            )
        }
    }
}

@Composable
@NonRestartableComposable
fun ProjectCard(
    modifier: Modifier,
    project: Project
) {
    Card(
        modifier = modifier
    ) {
        Text(
            text = project.name,
            fontFamily = displayFontFamily,
            fontSize = 18.sp
        )
    }
}

private fun String.asVersionText() : String {
    return if(this.startsWith(VERSION_PREFIX))
        return this
    else
        "$VERSION_PREFIX$this"
}