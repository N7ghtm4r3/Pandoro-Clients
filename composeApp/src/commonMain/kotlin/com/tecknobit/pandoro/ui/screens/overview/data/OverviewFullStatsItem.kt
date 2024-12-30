package com.tecknobit.pandoro.ui.screens.overview.data

import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.serialization.Serializable

/**
 * The [OverviewFullStatsItem] data class allow to represent a complete overview item data
 *
 * @property total The statistic about the total value
 * @property personal The statistic about the personal value
 * @property personalPercentage The statistic about the personal percentage value
 * @property group The statistic about the group value
 * @property groupPercentage The statistic about the group percentage value
 * @property byMe The statistic about the by-me value
 * @property byMePercentage The statistic about the by-me percentage value
 *
 * @author N7ghtm4r3 - Tecknobit
 */
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
