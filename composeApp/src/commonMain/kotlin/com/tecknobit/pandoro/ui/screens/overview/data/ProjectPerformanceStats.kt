package com.tecknobit.pandoro.ui.screens.overview.data

data class ProjectPerformanceStats(
    val id: String,
    val name: String,
    val updates: Int,
    val totalDevelopmentDays: Int,
    val averageDaysPerUpdate: Double
)
