@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.overview.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toNullableResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [OverviewScreenViewModel] is useful to obtain an overview for the
 * [com.tecknobit.pandoro.ui.screens.overview.presenter.OverviewScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 */
class OverviewScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_overview` state flow holds the overview data
     */
    private val _overview = MutableStateFlow<Overview?>(
        value = null
    )
    val overview: StateFlow<Overview?> = _overview

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    lateinit var sessionFlowState: SessionFlowState

    /**
     * Method to retrieve the data of an [Overview]
     */
    fun retrieveOverview() {
        viewModelScope.launch {
            requester.sendRequest(
                request = { getOverview() },
                onSuccess = { response ->
                    sessionFlowState.notifyOperational()
                    val overview = response.toNullableResponseData()
                    overview?.let {
                        _overview.value = Json.decodeFromJsonElement(it)
                    }
                },
                onFailure = { sessionFlowState.notifyUserDisconnected() },
                onConnectionError = {
                    sessionFlowState.notifyServerOffline()
                }
            )
        }
    }
    
}