package com.tecknobit.pandoro.ui.screens.project.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.screens.ItemScreen

class ProjectScreen(
    projectId: String
) : ItemScreen<Project, ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
    }

    @Composable
    @NonRestartableComposable
    override fun ScreenContent() {
    }

    @Composable
    @NonRestartableComposable
    override fun getItemName(): String {
        return item.value!!.name
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