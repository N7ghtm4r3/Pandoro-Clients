package com.tecknobit.pandoro.ui.screens.home.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _unreadChangelog = MutableStateFlow(
        value = 0
    )
    val unreadChangelog: StateFlow<Int> = _unreadChangelog

    fun countUnreadChangelogs() {
        execRefreshingRoutine(
            currentContext = HomeScreen::class.java,
            routine = {
                requester.sendWRequest(
                    request = {
                        getUnreadChangelogsCount()
                    },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _unreadChangelog.value = it.toResponseContent().toInt()
                    },
                    onFailure = {
                        setHasBeenDisconnectedValue(true)
                    },
                    onConnectionError = {
                        setServerOfflineValue(true)
                    }
                )
            },
            refreshDelay = 5000L
        )
    }

}