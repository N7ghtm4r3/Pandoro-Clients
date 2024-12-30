package com.tecknobit.pandoro.ui.screens.overview.data

import kotlinx.serialization.Serializable

/**
 * The [OverviewStatsItem] data class allow to represent a simple overview item data
 *
 * @property total The statistic about the total value
 * @property personal The statistic about the personal value
 * @property personalPercentage The statistic about the personal percentage value
 * @property group The statistic about the group value
 * @property groupPercentage The statistic about the group percentage value
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class OverviewStatsItem(
    val total: Int,
    val personal: Int,
    val personalPercentage: Double,
    val group: Int,
    val groupPercentage: Double
) {

    /**
     * Method to create a list to use in the chart
     *
     * @return the list to use in the chart as [List] of [Int]
     */
    fun toChartValues() : List<Int> {
        return listOf(personal, group)
    }

    /**
     * Method to create a list of percentages
     *
     * @return the list of percentages as [List] of [Double]
     */
    fun toPercentages() : List<Double> {
        return listOf(personalPercentage, groupPercentage)
    }

}