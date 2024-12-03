package com.tecknobit.pandoro.ui.screens.createproject.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

class CreateProjectScreenViewModel(
    val projectId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {


}