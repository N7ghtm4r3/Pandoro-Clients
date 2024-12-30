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

/**
 * The **HomeScreenViewModel** class is the support class used to manage the [HomeScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxbackend.FetcherManager
 * @see EquinoxViewModel
 */
class HomeScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * **_unreadChangelog** -> the current number of the unread changelogs of the
     * [com.tecknobit.pandoro.localUser]
     */
    private val _unreadChangelog = MutableStateFlow(
        value = 0
    )
    val unreadChangelog: StateFlow<Int> = _unreadChangelog

    /**
     * Method to retrieve the number of the unread changelogs of the [com.tecknobit.pandoro.localUser]
     * each 5 seconds will be requested that value
     */
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