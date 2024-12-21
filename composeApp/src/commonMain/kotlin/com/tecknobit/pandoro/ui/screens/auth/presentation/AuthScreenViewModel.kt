package com.tecknobit.pandoro.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxbackend.environment.helpers.EquinoxRequester
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxAuthViewModel
import com.tecknobit.pandoro.localUser

class AuthScreenViewModel: EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    // TODO: USE THE REAL ONE
    requester = object : EquinoxRequester(
        host = "g",
        connectionErrorMessage = "gaga"
    ) {

    },
    localUser = localUser
) {



}