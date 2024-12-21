package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

@Serializable
data class OverviewStatsItem(
    val total: Int,
    val personal: Int,
    val personalPercentage: Double,
    val group: Int,
    val groupPercentage: Double
) {

    fun toChartValues() : List<Int> {
        return listOf(personal, group)
    }

    fun toPercentages() : List<Double> {
        return listOf(personalPercentage, groupPercentage)
    }

}