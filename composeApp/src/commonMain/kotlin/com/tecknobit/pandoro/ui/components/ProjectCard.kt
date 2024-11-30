@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.components

import CircleDashedCheck
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.in_development_since
import pandoro.composeapp.generated.resources.update_completed_in
import pandoro.composeapp.generated.resources.update_completed_info
import pandoro.composeapp.generated.resources.update_in_progress_info

const val VERSION_PREFIX = "v"

@Composable
fun InDevelopmentProjectCard(
    inDevelopmentProject: InDevelopmentProject
) {
    val project = inDevelopmentProject.project
    val update = inDevelopmentProject.update
    Card(
        modifier = Modifier
            .size(
                width = 250.dp,
                height = 130.dp
            ),
        onClick = {
            // TODO: NAT TO PROJECT
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
            val updateCompleted = update.allChangeNotesCompleted()
            UpdateStatusIcon(
                update = update,
                updateCompleted = updateCompleted
            )
            val developmentDays = update.developmentDays()
            Text(
                text = pluralStringResource(
                    resource =  if(updateCompleted)
                            Res.plurals.update_completed_in
                        else
                            Res.plurals.in_development_since,
                    quantity = developmentDays,
                    developmentDays
                ),
                fontSize = 12.sp
            )
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
private fun UpdateStatusIcon(
    update: ProjectUpdate,
    updateCompleted: Boolean
) {
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = update.targetVersion.asVersionText()
        )
        val state = rememberTooltipState()
        val scope = rememberCoroutineScope()
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(
                        text = stringResource(
                            if(updateCompleted)
                                Res.string.update_completed_info
                            else
                                Res.string.update_in_progress_info
                        )
                    )
                }
            },
            state = state
        ) {
            IconButton(
                modifier = Modifier
                    .size(25.dp),
                onClick = {
                    scope.launch {
                        state.show()
                    }
                }
            ) {
                Icon(
                    imageVector = if(updateCompleted)
                        Icons.Default.NewReleases
                    else
                        CircleDashedCheck,
                    contentDescription = null
                )
            }
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