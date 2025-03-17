package com.tecknobit.pandoro.ui.screens.home.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.json.treatsAsInt
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The **HomeScreenViewModel** class is the support class used to manage the [HomeScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel

 * @see Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 */
class HomeScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_unreadChangelog` -> the current number of the unread changelogs of the
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
        retrieve(
            currentContext = HomeScreen::class,
            routine = {
                requester.sendRequest(
                    request = { getUnreadChangelogsCount() },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _unreadChangelog.value = it.toResponseData().treatsAsInt()
                    },
                    onFailure = { setHasBeenDisconnectedValue(true) },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            },
            refreshDelay = 5000L
        )
    }

}