package com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel

class ScheduleUpdateScreenViewModel(
    projectId: String
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var targetVersion: MutableState<String>

    lateinit var targetVersionError: MutableState<Boolean>


}