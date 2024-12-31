package com.tecknobit.pandoro.ui.screens.overview.components

import ChartLine
import ReservedLine
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewFullStatsItem
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewStatsItem
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.asText
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandoro.ui.theme.Yellow
import com.tecknobit.pandorocore.enums.UpdateStatus
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.Pie
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.group
import pandoro.composeapp.generated.resources.no_data_available
import pandoro.composeapp.generated.resources.personal
import pandoro.composeapp.generated.resources.published_by_me_info_text
import pandoro.composeapp.generated.resources.scheduled_by_me_info_text
import pandoro.composeapp.generated.resources.started_by_me_info_text
import pandoro.composeapp.generated.resources.total

/**
 * The card to display the overview data stats about an [com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate]
 *
 * @param modifier The modifier to apply to the card
 * @param overviewStats The stats to display
 */
@Composable
@NonRestartableComposable
fun UpdateOverviewCard(
    modifier: Modifier = Modifier,
    overviewStats: OverviewFullStatsItem
) {
    val status = overviewStats.status
    Card(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            text = status.asText(),
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        )
        OverviewCardContent(
            total = overviewStats.total,
            values = overviewStats.toChartValues(),
            percentages = overviewStats.toPercentages()
        )
        HorizontalDivider()
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            text = stringResource(
                resource = when(status) {
                    UpdateStatus.SCHEDULED -> Res.string.scheduled_by_me_info_text
                    UpdateStatus.IN_DEVELOPMENT -> Res.string.started_by_me_info_text
                    UpdateStatus.PUBLISHED -> Res.string.published_by_me_info_text
                },
                overviewStats.byMe, overviewStats.byMePercentage.format(2)
            ),
            fontSize = 12.sp
        )
    }
}

/**
 * The card to display the overview data stats
 *
 * @param modifier The modifier to apply to the card
 * @param pieSize The size of the pie chart
 * @param pieStroke The stroke to apply to the pie chart
 * @param title The title of the card
 * @param totalHeader The header text for the total value
 * @param actionIcon The representative icon for an action
 * @param action The action to execute when the [actionIcon] is clicked
 * @param overviewStats The stats to display
 */
@Composable
@NonRestartableComposable
fun OverviewCard(
    modifier: Modifier = Modifier,
    pieSize: Dp = 150.dp,
    pieStroke: Dp = 35.dp,
    title: StringResource,
    totalHeader: StringResource = Res.string.total,
    actionIcon: ImageVector = ReservedLine,
    action: (() -> Unit)? = null,
    overviewStats: OverviewStatsItem
) {
    Card(
        modifier = modifier
    ) {
        Row (
            modifier = Modifier
                .then(
                    if(action != null) {
                        Modifier.padding(
                            start = 10.dp
                        )
                    } else {
                        Modifier.padding(
                            all = 10.dp
                        )
                    }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(title),
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            action?.let {
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    IconButton(
                        onClick = action
                    ) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = null
                        )
                    }
                }
            }
        }
        OverviewCardContent(
            total = overviewStats.total,
            totalHeader = totalHeader,
            pieSize = pieSize,
            pieStroke = pieStroke,
            values = overviewStats.toChartValues(),
            percentages = overviewStats.toPercentages()
        )
    }
}

/**
 * The content of the overview cards
 *
 * @param total The total value
 * @param totalHeader The header text for the total value
 * @param pieSize The size of the pie chart
 * @param pieStroke The stroke to apply to the pie chart
 * @param values The values of the pie chart
 * @param percentages The percentages values of the pie chart
 */
@Composable
@NonRestartableComposable
private fun OverviewCardContent(
    total: Int,
    totalHeader: StringResource = Res.string.total,
    pieSize: Dp = 150.dp,
    pieStroke: Dp = 35.dp,
    values: List<Int>,
    percentages: List<Double>
) {
    HorizontalDivider()
    if(total == 0) {
        EmptyListUI(
            containerModifier = Modifier
                .padding(
                    bottom = 16.dp
                ),
            icon = ChartLine,
            subText = Res.string.no_data_available,
            textStyle = TextStyle(
                fontFamily = bodyFontFamily
            )
        )
    } else {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val pieColors = listOf(MaterialTheme.colorScheme.primary, Green())
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                Box (
                    contentAlignment = Alignment.Center
                ) {
                    PieOverview(
                        pieChartColors = pieColors,
                        pieSize = pieSize,
                        pieStroke = pieStroke,
                        stats = values
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(totalHeader),
                            fontSize = 14.sp,
                            fontFamily = displayFontFamily
                        )
                        Text(
                            text = total.toString()
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                PieLegend(
                    pieChartColors = pieColors,
                    values = values,
                    percentages = percentages
                )
            }
        }
    }
}

/**
 * The pie chart related to an overview data
 *
 * @param modifier The modifier to apply to the pie chart
 * @param pieSize The size of the pie chart
 * @param pieStroke The stroke to apply to the pie chart
 * @param pieChartColors The values of the pie chart
 * @param stats The stats to display on the chart
 */
@Composable
@NonRestartableComposable
private fun PieOverview(
    modifier: Modifier = Modifier,
    pieSize: Dp = 150.dp,
    pieStroke: Dp = 35.dp,
    pieChartColors: List<Color>,
    stats: List<Int>
) {
    val data by rememberSaveable {
        mutableStateOf(
            getStatsChartData(
                pieChartColors = pieChartColors,
                stats = stats
            )
        )
    }
    PieChart(
        modifier = modifier
            .padding(
                all = 16.dp
            )
            .size(pieSize),
        data = data,
        style = Pie.Style.Stroke(
            width = pieStroke
        ),
        scaleAnimEnterSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300)
    )
}

/**
 * Method to format the chart data for the pies chart
 *
 * @param pieChartColors The values of the pie chart
 * @param stats The stats to display on the chart
 *
 * @return the chart data as [List] of [Pie]
 */
private fun getStatsChartData(
    pieChartColors: List<Color>,
    stats: List<Int>
) : List<Pie> {
    val pies = arrayListOf<Pie>()
    pieChartColors.forEachIndexed { index, color ->
        pies.add(
            Pie(
                data = stats[index].toDouble(),
                color = color,
                selectedColor = color
            )
        )
    }
    return pies
}

/**
 * The pie legend to attach to the charts
 *
 * @param pieChartColors The values of the pie chart
 * @param values The values of the pie chart
 * @param percentages The percentages values of the pie chart
 */
@Composable
@NonRestartableComposable
private fun PieLegend(
    pieChartColors: List<Color>,
    values: List<Int>,
    percentages: List<Double>
) {
    values.forEachIndexed { index, value ->
        val existsOnPie = index < pieChartColors.size
        val color = if(existsOnPie)
            pieChartColors[index]
        else
            Yellow()
        Column (
            modifier = Modifier
                .padding(
                    top = 10.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Text(
                    text = stringResource(
                        resource = index.getStatsHeader()
                    ),
                    fontFamily = displayFontFamily
                )
            }
            Row (
                modifier = Modifier
                    .padding(
                        start = 20.dp
                    ),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = value.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${percentages[index].format(2)}%)",
                    fontSize = 14.sp,
                    color = color,
                    fontFamily = displayFontFamily
                )
            }
        }
    }
}

/**
 * Method to get the stats header based on the index
 *
 * @return the stats header as [StringResource]
 */
private fun Int.getStatsHeader() : StringResource {
    return when(this) {
        0 -> Res.string.personal
        else -> Res.string.group
    }
}