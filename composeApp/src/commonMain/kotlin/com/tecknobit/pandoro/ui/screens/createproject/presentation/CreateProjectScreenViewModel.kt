package com.tecknobit.pandoro.ui.screens.createproject.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.navigator

class CreateProjectScreenViewModel(
    val projectId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    fun workOnProject() {
        // TODO: MAKE THE REQUEST TO EDIT OR ADD THEN
        navigator.goBack()
    }

}