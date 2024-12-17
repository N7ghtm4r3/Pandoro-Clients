@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults.rememberPlainTooltipPositionProvider
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandoro.ui.theme.PieChartColors
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.DrawStyle
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.Pie
import ir.ehsannarmani.compose_charts.models.PopupProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.average_days
import pandoro.composeapp.generated.resources.average_development_days
import pandoro.composeapp.generated.resources.days
import pandoro.composeapp.generated.resources.development_days
import pandoro.composeapp.generated.resources.stats
import pandoro.composeapp.generated.resources.total_development_days

/**
 * **axisProperties** custom axis properties for the [ModalProjectStats]
 */
private val axisProperties = GridProperties.AxisProperties(
    enabled = false
)

private val contentBuilder: (Double) -> String = {
    "%.0f".format(it)
}

@Composable
@NonRestartableComposable
fun ModalProjectStats(
    state: SheetState,
    scope: CoroutineScope,
    project: Project,
    publishedUpdates: List<ProjectUpdate>
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp
                        ),
                    text = stringResource(Res.string.stats),
                    fontFamily = displayFontFamily,
                    fontSize = 25.sp
                )
                HorizontalDivider()
                DevelopmentDays(
                    project = project,
                    publishedUpdates = publishedUpdates
                )
                AverageDaysPerUpdate(
                    project = project,
                    publishedUpdates = publishedUpdates
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
fun ProjectsStats(
    project: Project,
    publishedUpdates: List<ProjectUpdate>
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card {
            DevelopmentDays(
                project = project,
                publishedUpdates = publishedUpdates
            )
        }
        Card {
            AverageDaysPerUpdate(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                project = project,
                publishedUpdates = publishedUpdates
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun DevelopmentDays(
    project: Project,
    publishedUpdates: List<ProjectUpdate>
) {
    val totalDevelopmentDays = project.calculateTotalDevelopmentDays()
    StatsSection(
        modifier = Modifier
            .padding(
                top = 10.dp
            ),
        header = Res.string.development_days,
        subText = stringResource(
            Res.string.total_development_days,
            totalDevelopmentDays
        ),
        chart = {
            val colors = PieChartColors()
            var data by remember {
                mutableStateOf(
                    getTotalDevelopmentDaysData(
                        pieChartColors = colors,
                        publishedUpdates = publishedUpdates
                    )
                )
            }
            val currentSelectedPie = remember {
                mutableStateOf(
                    data[0]
                )
            }
            val tooltipState = rememberTooltipState()
            val tooltipScope = rememberCoroutineScope()
            TooltipBox(
                modifier = Modifier
                    .fillMaxWidth(),
                positionProvider = rememberPlainTooltipPositionProvider(),
                tooltip = {
                    PlainTooltip {
                        Column {
                            Text(
                                text = currentSelectedPie.value.label!!
                            )
                            val days = currentSelectedPie.value.data.toInt()
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp)
                            ) {
                                Text(
                                    text = pluralStringResource(
                                        resource = Res.plurals.days,
                                        quantity = days,
                                        days
                                    ),
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "(${(days * 100.0 / totalDevelopmentDays)
                                        .format(2)})%",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                state = tooltipState
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    PieChart(
                        modifier = Modifier
                            .size(150.dp),
                        data = data,
                        onPieClick = {
                            val pieIndex = data.indexOf(it)
                            data = data.mapIndexed { mapIndex, pie ->
                                pie.copy(
                                    selected = pieIndex == mapIndex
                                )
                            }
                            currentSelectedPie.value = data[pieIndex]
                            tooltipScope.launch {
                                tooltipState.show()
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
            }
        }
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
        println(colorSelectorIndex)
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
}

@Composable
@NonRestartableComposable
private fun AverageDaysPerUpdate(
    modifier: Modifier = Modifier,
    project: Project,
    publishedUpdates: List<ProjectUpdate>
) {
    val averageDaysPerUpdate = project.calculateAverageDaysPerUpdate()
    StatsSection(
        modifier = modifier,
        header = Res.string.average_development_days,
        subText = pluralStringResource(
            resource = Res.plurals.average_days,
            quantity = averageDaysPerUpdate.toInt(),
            averageDaysPerUpdate
        ),
        chart = {
            val lineColor = Green()
            LineChart(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
                    .fillMaxWidth()
                    .height(250.dp),
                gridProperties = GridProperties(
                    enabled = false,
                    xAxisProperties = axisProperties,
                    yAxisProperties = axisProperties
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = true,
                    textStyle = TextStyle(
                        color = contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ),
                    contentBuilder = contentBuilder
                ),
                popupProperties = PopupProperties(
                    textStyle = TextStyle(
                        color = contentColorFor(
                            backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ),
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentBuilder = contentBuilder
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = false
                ),
                labelProperties = LabelProperties(
                    enabled = true
                ),
                data = remember {
                    getAverageDaysPerUpdateData(
                        lineColor = lineColor,
                        publishedUpdates = publishedUpdates
                    )
                }
            )
        }
    )
}

private fun getAverageDaysPerUpdateData(
    lineColor: Color,
    publishedUpdates: List<ProjectUpdate>
) : List<Line> {
    val chartData = arrayListOf<Double>()
    publishedUpdates.forEach { update ->
        chartData.add(update.developmentDays().toDouble())
    }
    return listOf(
        Line(
            label = "",
            values = chartData,
            color = SolidColor(lineColor),
            firstGradientFillColor = lineColor.copy(
                alpha = .7f
            ),
            secondGradientFillColor = Color.Transparent,
            drawStyle = DrawStyle.Fill
        )
    )
}

@Composable
@NonRestartableComposable
private fun StatsSection(
    modifier: Modifier = Modifier,
    header: StringResource,
    subText: String,
    chart: @Composable () -> Unit
) {
    Text(
        modifier = modifier
            .padding(
                start = 16.dp
            ),
        fontFamily = displayFontFamily,
        text = stringResource(header)
    )
    Text(
        modifier = Modifier
            .padding(
                start = 16.dp
            ),
        text = subText,
        fontSize = 14.sp
    )
    chart.invoke()
}