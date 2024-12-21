package com.tecknobit.pandoro.ui.screens.overview.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewStatsItem
import com.tecknobit.pandoro.ui.theme.Green
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.Pie
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.group
import pandoro.composeapp.generated.resources.personal
import pandoro.composeapp.generated.resources.total

@Composable
@NonRestartableComposable
fun OverviewCard(
    modifier: Modifier = Modifier,
    title: StringResource,
    overviewStats: OverviewStatsItem
) {
    Card(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            text = stringResource(title),
            fontFamily = displayFontFamily,
            fontSize = 20.sp
        )
        HorizontalDivider()
        Row (
            verticalAlignment = Alignment.CenterVertically
        ) {
            val pieColors = listOf(MaterialTheme.colorScheme.primary, Green())
            Box (
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                PieOverview(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    pieChartColors = pieColors,
                    stats = overviewStats
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(Res.string.total),
                        fontSize = 14.sp,
                        fontFamily = displayFontFamily
                    )
                    Text(
                        text = "${overviewStats.total}"
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                PieLegend(
                    pieChartColors = pieColors,
                    overviewStats = overviewStats
                )
            }
        }
    }
}


@Composable
@NonRestartableComposable
private fun PieOverview(
    modifier: Modifier,
    pieChartColors: List<Color>,
    stats: OverviewStatsItem
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
            .size(150.dp),
        data = data,
        style = Pie.Style.Stroke(
            width = 35.dp
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

private fun getStatsChartData(
    pieChartColors: List<Color>,
    stats: OverviewStatsItem
) : List<Pie> {
    val pies = arrayListOf<Pie>()
    pieChartColors.forEachIndexed { index, color ->
        pies.add(
            Pie(
                data = stats.relatedStats(
                    index = index
                ).toDouble(),
                color = color,
                selectedColor = color
            )
        )
    }
    return pies
}

@Composable
@NonRestartableComposable
private fun PieLegend(
    pieChartColors: List<Color>,
    overviewStats: OverviewStatsItem
) {
    pieChartColors.forEachIndexed { index, color ->
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
                    text = "${overviewStats.relatedStats(index)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "(${overviewStats.groupPercentage.format(2)}%)",
                    fontSize = 14.sp,
                    color = color,
                    fontFamily = displayFontFamily
                )
            }
        }
    }
}

private fun Int.getStatsHeader() : StringResource {
    return when(this) {
        0 -> Res.string.personal
        else -> Res.string.group
    }
}