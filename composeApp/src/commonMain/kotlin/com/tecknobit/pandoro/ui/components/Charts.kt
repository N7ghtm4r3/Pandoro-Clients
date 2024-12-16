@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.theme.Green
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.stats

@Composable
@NonRestartableComposable
fun ProjectStatsChart(
    state: SheetState,
    scope: CoroutineScope,
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
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp
                    ),
                text = stringResource(Res.string.stats),
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            HorizontalDivider()
            val textStyle = TextStyle(
                fontFamily = bodyFontFamily,
                color = contentColorFor(
                    backgroundColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
            val barColor = Green()
            ColumnChart(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
                    .height(200.dp),
                data = remember {
                    getStatsList(
                        barColor = barColor,
                        publishedUpdates = publishedUpdates
                    )
                },
                barProperties = BarProperties(
                    cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                    spacing = 0.dp,
                    thickness = 25.dp
                ),
                labelHelperProperties = LabelHelperProperties(
                    enabled = false
                ),
                gridProperties = GridProperties(
                    enabled = false,
                    yAxisProperties = GridProperties.AxisProperties(
                        enabled = false,
                    )
                ),
                indicatorProperties = HorizontalIndicatorProperties(
                    enabled = true,
                    contentBuilder = {
                        "%.0f".format(it)
                    }
                ),
                dividerProperties = DividerProperties(
                    enabled = false,
                    yAxisProperties = LineProperties(
                        enabled = false
                    )
                ),
                popupProperties = PopupProperties(
                    textStyle = textStyle,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentBuilder = {
                        "%.0f".format(it)
                    }
                ),
                labelProperties = LabelProperties(
                    enabled = true,
                    textStyle = textStyle.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    padding = 10.dp
                )
            )
        }
    }
}

private fun getStatsList(
    barColor: Color,
    publishedUpdates: List<ProjectUpdate>
) : List<Bars> {
    val bars = arrayListOf<Bars>()
    publishedUpdates.forEach { update ->
        bars.add(
            Bars(
                label = update.targetVersion.asVersionText(),
                values = listOf(
                    Bars.Data(
                        value = update.developmentDays().toDouble(),
                        color = SolidColor(barColor)
                    )
                )
            )
        )
    }
    return bars
}