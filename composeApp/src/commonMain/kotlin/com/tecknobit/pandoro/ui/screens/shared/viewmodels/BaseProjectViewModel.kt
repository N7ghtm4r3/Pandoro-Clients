package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class BaseProjectViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _project = MutableStateFlow<Project?>(
        value = null
    )
    val project: StateFlow<Project?> = _project

    abstract fun retrieveProject()

}