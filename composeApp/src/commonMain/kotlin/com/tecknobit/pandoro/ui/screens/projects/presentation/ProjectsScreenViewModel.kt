package com.tecknobit.pandoro.ui.screens.projects.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class ProjectsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _inDevelopmentProjects = MutableStateFlow(
        value = emptyList()
    )

}