package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

@Serializable
data class Overview(
    val totalProjects: OverviewStatsItem,
    val totalUpdates: OverviewStatsItem,
    val updatesScheduled: OverviewFullStatsItem,
    val updatesInDevelopment: OverviewFullStatsItem,
    val updatesPublished: OverviewFullStatsItem,
    val developmentDays: OverviewStatsItem,
    val averageDevelopmentDays: OverviewStatsItem,
    val bestPersonalPerformanceProject: ProjectPerformanceStats,
    val worstPersonalPerformanceProject: ProjectPerformanceStats? = null,
    val bestGroupPerformanceProject: ProjectPerformanceStats? = null,
    val worstGroupPerformanceProject: ProjectPerformanceStats? = null
) {

    fun updatesAsIterable() : List<OverviewFullStatsItem> {
        return listOf(updatesScheduled, updatesInDevelopment, updatesPublished)
    }

}