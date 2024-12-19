package com.tecknobit.pandoro.ui.screens.overview.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.overview.data.OverviewStatsItem
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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
        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "140",
                        fontSize = 14.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Personali",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "140 (100%)",
                        fontSize = 14.sp
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Gruppo",
                        fontSize = 12.sp
                    )
                    Text(
                        text = "140 (100%)",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/*
@Composable
@NonRestartableComposable
private fun PieOverview() {
    val pieColors = listOf(MaterialTheme.colorScheme.primary, Green())
    var data by rememberSaveable {
        mutableStateOf(
            getTotalDevelopmentDaysData(
                pieChartColors = colors,
                publishedUpdates = publishedUpdates
            )
        )
    }
    PieChart(
        modifier = Modifier
            .size(200.dp),
        data = data,
        onPieClick = {
            val pieIndex = data.indexOf(it)
            data = data.mapIndexed { mapIndex, pie ->
                pie.copy(
                    selected = pieIndex == mapIndex
                )
            }
        },
        scaleAnimEnterSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        colorAnimEnterSpec = tween(300),
        colorAnimExitSpec = tween(300),
        scaleAnimExitSpec = tween(300),
        spaceDegreeAnimExitSpec = tween(300)
    )
}

private fun getTotalDevelopmentDaysData(
    pieChartColors: Array<Color>,
    publishedUpdates: List<ProjectUpdate>
) : List<Pie> {
    val pies = arrayListOf<Pie>()
    val colors = pieChartColors.size
    publishedUpdates.forEachIndexed { index, update ->
        val colorSelectorIndex = if(index > colors)
            index % colors
        else
            index
        val color = pieChartColors[colorSelectorIndex]
        pies.add(
            Pie(
                label = update.targetVersion.asVersionText(),
                data = update.developmentDays().toDouble(),
                color = color,
                selectedColor = color
            )
        )
    }
    return pies
}*/