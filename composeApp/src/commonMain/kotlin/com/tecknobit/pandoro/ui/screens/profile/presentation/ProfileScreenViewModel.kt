package com.tecknobit.pandoro.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxbackend.environment.helpers.EquinoxRequester
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel
import com.tecknobit.pandoro.localUser

class ProfileScreenViewModel: EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    // TODO: USE THE REAL ONE
    requester = object : EquinoxRequester(
        host = "g",
        connectionErrorMessage = "gaga"
    ) {

    },
    localUser = localUser
) {

    lateinit var profilePic: MutableState<String>

}