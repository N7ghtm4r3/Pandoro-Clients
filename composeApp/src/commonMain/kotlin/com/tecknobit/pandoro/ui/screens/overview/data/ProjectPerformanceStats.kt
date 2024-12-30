package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

/**
 * The [ProjectPerformanceStats] data class allow to represent a the performance stats about a project
 *
 * @property id The identifier of the project
 * @property name The name of the project
 * @property updates The total number of the updates of the project
 * @property totalDevelopmentDays The total number of the development days of the project
 * @property averageDaysPerUpdate The average number of the development days of the project
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class ProjectPerformanceStats(
    val id: String,
    val name: String,
    val updates: Int,
    val totalDevelopmentDays: Int,
    val averageDaysPerUpdate: Double
)
