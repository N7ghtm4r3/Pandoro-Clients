package com.tecknobit.pandoro.ui.screens.overview.data

data class Overview(
    val totalProjects: OverviewStatsItem,
    val totalUpdates: OverviewStatsItem,
    val updatesScheduled: OverviewFullStatsItem,
    val updatesInDevelopment: OverviewFullStatsItem,
    val updatesPublished: OverviewFullStatsItem,
    val developmentDays: OverviewStatsItem,
    val averageDevelopmentDays: OverviewStatsItem,
    val bestPersonalPerformanceProject: ProjectPerformanceStats,
    val worstPersonalPerformanceProject: ProjectPerformanceStats,
    val bestGroupPerformanceProject: ProjectPerformanceStats? = null,
    val worstGroupPerformanceProject: ProjectPerformanceStats? = null
)