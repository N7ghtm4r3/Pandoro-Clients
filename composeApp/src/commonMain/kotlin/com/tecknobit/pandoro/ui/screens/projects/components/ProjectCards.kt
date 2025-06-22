@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)

package com.tecknobit.pandoro.ui.screens.projects.components

import CircleDashedCheck
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NewReleases
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.HorizontalProgressBar
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.PROJECT_SCREEN
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteProject
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.group.components.GroupLogos
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.in_development_since
import pandoro.composeapp.generated.resources.update_completed_in
import pandoro.composeapp.generated.resources.update_completed_info
import pandoro.composeapp.generated.resources.update_in_progress_info

/**
 * Custom [Card] to display a project currently [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT]
 *
 * @param inDevelopmentProject The in development project to display
 */
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
                height = 150.dp
            ),
        onClick = {
            /*HomeScreen.setCurrentScreenDisplayed(
                screen = PROJECTS_SCREEN
            )*/
            navigator.navigate("$PROJECT_SCREEN/${project.id}/${update.id}")
        }
    ) {
        Column (
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            ProjectTitle(
                project = project
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
            Column (
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                HorizontalProgressBar(
                    completionWidth = 230.dp,
                    currentProgress = update.notes.filter { note -> note.markedAsDone }.size,
                    total = update.notes.size,
                )
            }
        }
    }
}

/**
 * The custom icon to represent the current status of the project such currently in development or
 * ready to be published
 *
 * @param update The update [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT] of the project
 * @param updateCompleted Whether the update is ready to be published
 */
@Composable
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

/**
 * Custom [Card] to display a project item
 *
 * @param viewModel The support viewmodel for the screen
 * @param modifier The modifier to apply to the [Card]
 * @param project The project to display
 */
@Composable
fun ProjectCard(
    viewModel: ProjectsScreenViewModel,
    modifier: Modifier,
    project: Project
) {
    val amITheProjectAuthor = project.amITheProjectAuthor()
    var editProject by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = {
                    /*HomeScreen.setCurrentScreenDisplayed(
                        screen = PROJECTS_SCREEN
                    )*/
                    navigator.navigate("$PROJECT_SCREEN/${project.id}")
                },
                onLongClick = if(amITheProjectAuthor) {
                    {
                        editProject = true
                    }
                } else
                    null
            )
    ) {
        val showCardFooter = amITheProjectAuthor || project.isSharedWithGroups()
        Column (
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            Text(
                text = project.version.asVersionText(),
                fontSize = 12.sp
            )
            ProjectHeader(
                project = project
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = project.description,
                overflow = TextOverflow.Ellipsis,
                minLines = 3,
                maxLines = if(showCardFooter)
                    3
                else
                    Int.MAX_VALUE,
                fontSize = 14.sp
            )
        }
        if(showCardFooter) {
            HorizontalDivider()
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(
                        min = 50.dp
                    )
                    .padding(
                        start = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroupLogos(
                    project = project
                )
                if(amITheProjectAuthor) {
                    val deleteProject = remember { mutableStateOf(false) }
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        IconButton(
                            onClick =  { deleteProject.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    DeleteProject(
                        viewModel = viewModel,
                        show = deleteProject,
                        project = project,
                        deleteRequest = {
                            viewModel.deleteProject(
                                project = project,
                                onDelete = {
                                    deleteProject.value = false
                                    viewModel.inDevelopmentProjectsState.refresh()
                                    viewModel.projectsState.refresh()
                                },
                                onFailure = {
                                    viewModel.showSnackbarMessage(it)
                                }
                            )
                        }
                    )
                }
            }
        }
    }
    if(editProject)
        navigator.navigate("$CREATE_PROJECT_SCREEN/${project.id}")
}

/**
 * Custom header for the [ProjectCard]
 *
 * @param project The project displayed
 */
@Composable
@NonRestartableComposable
private fun ProjectHeader(
    project: Project
) {
    Row (
        modifier = Modifier
            .heightIn(
                min = 35.dp
            ),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        project.icon?.let { icon ->
            Thumbnail(
                thumbnailData = icon,
                contentDescription = "Project icon",
            )
        }
        ProjectTitle(
            project = project
        )
    }
}

/**
 * Custom title component for the [ProjectCard]
 *
 * @param modifier The modifier to apply to the component
 * @param project The project displayed
 */
@Composable
@NonRestartableComposable
private fun ProjectTitle(
    modifier: Modifier = Modifier,
    project: Project
) {
    Text(
        modifier = modifier,
        text = project.name,
        fontFamily = displayFontFamily,
        fontSize = 20.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}