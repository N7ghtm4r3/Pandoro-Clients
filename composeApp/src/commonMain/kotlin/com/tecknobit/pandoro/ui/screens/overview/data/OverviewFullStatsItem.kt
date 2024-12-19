package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

@Serializable
data class OverviewFullStatsItem(
    val total: Int,
    val personal: Int,
    val personalPercentage: Double,
    val group: Int,
    val groupPercentage: Double,
    val byMe: Int,
    val byMePercentage: Double
)
