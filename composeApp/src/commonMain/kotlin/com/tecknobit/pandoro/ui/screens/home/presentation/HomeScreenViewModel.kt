package com.tecknobit.pandoro.ui.screens.home.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class HomeScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    companion object {

        private const val ONE_MINUTE = 60000L

    }

    private val _unreadChangelog = MutableStateFlow(
        value = 0
    )
    val unreadChangelog: StateFlow<Int> = _unreadChangelog

    fun countUnreadChangelogs() {
        execRefreshingRoutine(
            currentContext = HomeScreen::class.java,
            routine = {
                // TODO: MAKE THE REQUEST THEN
                _unreadChangelog.value = Random.nextInt(101)
            },
            refreshDelay = ONE_MINUTE
        )
    }

}