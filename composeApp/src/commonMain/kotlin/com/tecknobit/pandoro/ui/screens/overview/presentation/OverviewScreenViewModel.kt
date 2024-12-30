package com.tecknobit.pandoro.ui.screens.overview.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toNullResponseData
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
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
     * **_overview** -> state flow holds the overview data
     */
    private val _overview = MutableStateFlow<Overview?>(
        value = null
    )
    val overview: StateFlow<Overview?> = _overview

    /**
     * Method to retrieve the data of an [Overview]
     */
    fun retrieveOverview() {
        viewModelScope.launch {
            requester.sendWRequest(
                request = { getOverview() },
                onSuccess = { response ->
                    val overview = response.toNullResponseData()
                    overview?.let {
                        _overview.value = Json.decodeFromJsonElement(it)
                    }
                },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }
    
}