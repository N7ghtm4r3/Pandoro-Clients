@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.projects.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.projects.data.InDevelopmentProject
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.in_development
import pandoro.composeapp.generated.resources.projects

class ProjectsScreen: PandoroScreen<ProjectsScreenViewModel>(
    viewModel = ProjectsScreenViewModel()
) {

    private lateinit var inDevelopmentProjects: State<List<InDevelopmentProject>>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.primary,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
            bottomBar = { AdaptBottomBarToNavigationMode() }
        ) {
            AdaptContentToNavigationMode {
                Text(
                    text = stringResource(Res.string.projects),
                    fontFamily = displayFontFamily,
                    fontSize = 35.sp
                )
                ProjectsInDevelopmentSection()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsInDevelopmentSection() {
        Column {
            Text(
                text = stringResource(Res.string.in_development)
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(
                    items = inDevelopmentProjects.value,
                    key = { inDevelopmentProject -> inDevelopmentProject.update.id }
                ) { inDevelopmentProject ->
                    val project = inDevelopmentProject.project
                    val update = inDevelopmentProject.update
                    Card(

                    ) {
                        Text(
                            text = project.name
                        )
                    }
                }
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        inDevelopmentProjects = viewModel!!.inDevelopmentProject.collectAsState()
    }

}