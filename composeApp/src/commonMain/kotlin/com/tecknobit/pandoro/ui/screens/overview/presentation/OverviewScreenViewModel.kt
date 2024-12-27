package com.tecknobit.pandoro.ui.screens.overview.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewFullStatsItem
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewStatsItem
import com.tecknobit.pandoro.ui.screens.overview.data.ProjectPerformanceStats
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class OverviewScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _overview = MutableStateFlow<Overview?>(
        value = null
    )
    val overview: StateFlow<Overview?> = _overview

    fun retrieveOverview() {
        viewModelScope.launch {
            // TODO: MAKE THE REQUEST THEN
            _overview.value = Overview(
                totalProjects = OverviewStatsItem(
                    total = 10,
                    personal = 1000000,
                    personalPercentage = 2.2,
                    group = 20000,
                    groupPercentage = 100.0
                ),
                totalUpdates = OverviewStatsItem(
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5
                ),
                updatesScheduled = OverviewFullStatsItem(
                    status = UpdateStatus.SCHEDULED,
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5,
                    byMe = 2,
                    byMePercentage = 2.5
                ),
                updatesInDevelopment = OverviewFullStatsItem(
                    status = UpdateStatus.IN_DEVELOPMENT,
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5,
                    byMe = 2,
                    byMePercentage = 2.5
                ),
                updatesPublished = OverviewFullStatsItem(
                    status = UpdateStatus.PUBLISHED,
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5,
                    byMe = 2,
                    byMePercentage = 2.5
                ),
                bestPersonalPerformanceProject = ProjectPerformanceStats(
                    id = Random.nextLong().toString(),
                    name = "gew",
                    updates = 1,
                    totalDevelopmentDays = 1,
                    averageDaysPerUpdate = 1.2
                ),
                worstPersonalPerformanceProject = ProjectPerformanceStats(
                    id = Random.nextLong().toString(),
                    name = "g",
                    updates = 1,
                    totalDevelopmentDays = 1,
                    averageDaysPerUpdate = 1.2
                ),
                bestGroupPerformanceProject = ProjectPerformanceStats(
                    id = Random.nextLong().toString(),
                    name = "gew",
                    updates = 1,
                    totalDevelopmentDays = 1,
                    averageDaysPerUpdate = 1.2
                ),
                worstGroupPerformanceProject = ProjectPerformanceStats(
                    id = Random.nextLong().toString(),
                    name = "g",
                    updates = 1,
                    totalDevelopmentDays = 1,
                    averageDaysPerUpdate = 1.2
                ),
                developmentDays = OverviewStatsItem(
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5
                ),
                averageDevelopmentDays = OverviewStatsItem(
                    total = 10,
                    personal = 2,
                    personalPercentage = 2.2,
                    group = 2,
                    groupPercentage = 2.5
                )
            )
        }
    }
    
}