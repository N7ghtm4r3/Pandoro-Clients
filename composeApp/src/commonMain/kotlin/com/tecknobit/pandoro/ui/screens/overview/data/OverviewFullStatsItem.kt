package com.tecknobit.pandoro.ui.screens.overview.data

import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.serialization.Serializable

@Serializable
data class OverviewFullStatsItem(
    val status: UpdateStatus,
    val total: Int,
    val personal: Int,
    val personalPercentage: Double,
    val group: Int,
    val groupPercentage: Double,
    val byMe: Int,
    val byMePercentage: Double
) {

    fun toChartValues() : List<Int> {
        return listOf(personal, group)
    }

    fun toPercentages() : List<Double> {
        return listOf(personalPercentage, groupPercentage)
    }

}
