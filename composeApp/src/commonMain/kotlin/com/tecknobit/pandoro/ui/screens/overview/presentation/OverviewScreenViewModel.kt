package com.tecknobit.pandoro.ui.screens.overview.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewFullStatsItem
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewStatsItem
import com.tecknobit.pandoro.ui.screens.overview.data.ProjectPerformanceStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OverviewScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    private val _overview = MutableStateFlow<Overview?>(
        value = null
    )
    val overview: StateFlow<Overview?> = _overview

    fun retrieveOverview() {
        // TODO: MAKE THE REQUEST THEN
        _overview.value = Overview(
            totalProjects = OverviewStatsItem(
                total = 10,
                personal = 2,
                personalPercentage = 2.2,
                group = 2,
                groupPercentage = 2.5
            ),
            totalUpdates = OverviewStatsItem(
                total = 10,
                personal = 2,
                personalPercentage = 2.2,
                group = 2,
                groupPercentage = 2.5
            ),
            updatesScheduled = OverviewFullStatsItem(
                total = 10,
                personal = 2,
                personalPercentage = 2.2,
                group = 2,
                groupPercentage = 2.5,
                byMe = 2,
                byMePercentage = 2.5
            ),
            updatesInDevelopment = OverviewFullStatsItem(
                total = 10,
                personal = 2,
                personalPercentage = 2.2,
                group = 2,
                groupPercentage = 2.5,
                byMe = 2,
                byMePercentage = 2.5
            ),
            updatesPublished = OverviewFullStatsItem(
                total = 10,
                personal = 2,
                personalPercentage = 2.2,
                group = 2,
                groupPercentage = 2.5,
                byMe = 2,
                byMePercentage = 2.5
            ),
            bestPersonalPerformanceProject = ProjectPerformanceStats(
                name = "gew",
                updates = 1,
                totalDevelopmentDays = 1,
                averageDaysPerUpdate = 1.2
            ),
            worstPersonalPerformanceProject = ProjectPerformanceStats(
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