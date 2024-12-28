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

class OverviewScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _overview = MutableStateFlow<Overview?>(
        value = null
    )
    val overview: StateFlow<Overview?> = _overview

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