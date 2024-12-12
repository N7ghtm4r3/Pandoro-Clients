package com.tecknobit.pandoro.ui.screens.project.presenter

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteProject
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.group.components.GroupIcons
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.shared.screens.ItemScreen
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.edit
import pandoro.composeapp.generated.resources.github
import pandoro.composeapp.generated.resources.gitlab
import pandoro.composeapp.generated.resources.updates

class ProjectScreen(
    projectId: String
) : ItemScreen<Project, ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun ItemScreenTitle() {
        Column(
            modifier = Modifier
                .widthIn(
                    max = FORM_CARD_WIDTH
                )
                .fillMaxWidth(),
        ) {
            ProjectTitle()
            ProjectInformation()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectTitle() {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ScreenTitle(
                navBackAction = { navigator.goBack() },
                title = item.value!!.name
            )
            item.value!!.getRepositoryPlatform()?.let { platform ->
                val uriHandler = LocalUriHandler.current
                IconButton(
                    onClick =  {
                        uriHandler.openUri(
                            uri = item.value!!.projectRepo
                        )
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .then(
                                when(platform) {
                                    RepositoryPlatform.Github -> Modifier
                                    RepositoryPlatform.GitLab -> Modifier
                                        .padding(
                                            top = 10.dp
                                        )
                                        .size(30.dp)
                                }
                            ),
                        painter = painterResource(
                            when(platform) {
                                RepositoryPlatform.Github -> Res.drawable.github
                                RepositoryPlatform.GitLab -> Res.drawable.gitlab
                            }
                        ),
                        contentDescription = "Platform Icon"
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectInformation() {
        Row(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                )
                .padding(
                    start = 10.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            item.value!!.icon?.let { icon ->
                Thumbnail(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            shape = CircleShape
                        ),
                    size = 100.dp,
                    thumbnailData = icon,
                    contentDescription = "Project Icon"
                )
                Column {
                    Column (
                        verticalArrangement = Arrangement.Center
                    ) {
                        GroupIcons(
                            modifier = Modifier
                                .heightIn(
                                    min = 30.dp
                                ),
                            project = item.value!!
                        )
                        Text(
                            text = item.value!!.version.asVersionText()
                        )
                    }
                    if(item.value!!.amITheProjectAuthor())
                        ActionButtons()
                    else
                        ProjectAuthor()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ActionButtons() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = {
                    navigator.navigate("$CREATE_PROJECT_SCREEN/${item.value!!.id}")
                }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.edit),
                    fontSize = 12.sp,
                    backgroundColor = MaterialTheme.colorScheme.inversePrimary
                )
            }
            val deleteProject = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { deleteProject.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    fontSize = 12.sp
                )
            }
            DeleteProject(
                viewModel = viewModel!!,
                project = item.value!!,
                show = deleteProject,
                deleteRequest = { project ->
                    viewModel!!.deleteProject(
                        project = project,
                        onDelete = {
                            deleteProject.value = false
                            navigator.goBack()
                        }
                    )
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectAuthor() {
        val author = item.value!!.author
        Text(
            text = author.completeName(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Text(
            text = author.email,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 12.sp
        )
    }

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.ScreenContent() {
        Section(
            modifier = Modifier
                .padding(
                    start = 10.dp
                ),
            header = Res.string.updates
        ) {

        }
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = {
                // TODO: SCHEDULE UPDATE
            }
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        item = viewModel!!.project.collectAsState()
    }

}