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

    fun relatedStats(
        index: Int
    ) : Int {
        return when(index) {
            0 -> personal
            else -> group
        }
    }

}