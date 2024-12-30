package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

/**
 * The [Overview] data class allow to represent a overview data
 *
 * @property totalProjects The statistic about the total project of the [com.tecknobit.pandoro.localUser]
 * @property totalUpdates The statistic about the total updates of the [com.tecknobit.pandoro.localUser]
 * @property updatesScheduled The statistic about the updates scheduled of the [com.tecknobit.pandoro.localUser]
 * @property updatesInDevelopment The statistic about the updates in development of the [com.tecknobit.pandoro.localUser]
 * @property updatesPublished The statistic about the updates published of the [com.tecknobit.pandoro.localUser]
 * @property developmentDays The statistic about the total development days of the [com.tecknobit.pandoro.localUser]
 * @property averageDevelopmentDays The statistic about the average development days of the [com.tecknobit.pandoro.localUser]
 * @property bestPersonalPerformanceProject The statistic about the best personal project of the [com.tecknobit.pandoro.localUser]
 * @property worstPersonalPerformanceProject The statistic about the worst personal project of the [com.tecknobit.pandoro.localUser]
 * @property bestGroupPerformanceProject The statistic about the best group project of the [com.tecknobit.pandoro.localUser]
 * @property worstGroupPerformanceProject The statistic about the worst group project of the [com.tecknobit.pandoro.localUser]
 *
 * @author N7ghtm4r3 - Tecknobit
 */
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

    /**
     * Method to obtain the list of the stats about the updates
     *
     * @return the list of the stats about the updates as [List] of [OverviewFullStatsItem]
     */
    fun updatesAsIterable() : List<OverviewFullStatsItem> {
        return listOf(updatesScheduled, updatesInDevelopment, updatesPublished)
    }

}