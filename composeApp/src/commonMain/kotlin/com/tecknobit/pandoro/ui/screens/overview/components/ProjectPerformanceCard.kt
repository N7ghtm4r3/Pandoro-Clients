package com.tecknobit.pandoro.ui.screens.overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.pandoro.PROJECT_SCREEN
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen.Companion.OVERVIEW_SCREEN
import com.tecknobit.pandoro.ui.screens.overview.data.ProjectPerformanceStats
import com.tecknobit.pandoro.ui.theme.Green
import ir.ehsannarmani.compose_charts.extensions.format
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.best
import pandoro.composeapp.generated.resources.days_per_update
import pandoro.composeapp.generated.resources.development_days
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.to_improve
import pandoro.composeapp.generated.resources.updates

/**
 * Card to display the performance of a project
 *
 * @param modifier The modifier to apply to the component
 * @param project The stats about the performance of the project
 * @param isTheBest Whether the [project] is the best in terms of performance
 */
@Composable
fun ProjectPerformanceCard(
    modifier: Modifier = Modifier,
    project: ProjectPerformanceStats,
    isTheBest: Boolean
) {
    Card(
        modifier = modifier
            .colorOneSideBorder(
                borderToColor = BorderToColor.END,
                color = if(isTheBest)
                    Green()
                else
                    MaterialTheme.colorScheme.error,
                width = 8.dp,
                shape = CardDefaults.shape
            ),
        onClick = {
            HomeScreen.setCurrentScreenDisplayed(
                screen = OVERVIEW_SCREEN
            )
            navigator.navigate("$PROJECT_SCREEN/${project.id}")
        }
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            text = stringResource(
                if(isTheBest)
                    Res.string.best
                else
                    Res.string.to_improve
            ),
            fontFamily = displayFontFamily,
            fontSize = 17.sp
        )
        HorizontalDivider()
        Column (
            modifier = Modifier
                .padding(
                    all = 10.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row {
                StatInfo(
                    modifier = Modifier
                        .weight(1f),
                    header = Res.string.name,
                    info = project.name
                )
                StatInfo(
                    modifier = Modifier
                        .weight(1f),
                    header = Res.string.updates,
                    info = project.updates
                )
            }
            Row {
                StatInfo(
                    modifier = Modifier
                        .weight(1f),
                    header = Res.string.development_days,
                    info = project.updates
                )
                StatInfo(
                    modifier = Modifier
                        .weight(1f),
                    header = Res.string.days_per_update,
                    info = project.averageDaysPerUpdate.format(2)
                )
            }
        }
    }
}

/**
 * The stats info section
 *
 * @param modifier The modifier to apply to the component
 * @param header The representative header of the stats section
 * @param info The info value of the statistic
 */
@Composable
@NonRestartableComposable
private fun StatInfo(
    modifier: Modifier = Modifier,
    header: StringResource,
    info: Int
) {
    StatInfo(
        modifier = modifier,
        header = header,
        info = info.toString()
    )
}

/**
 * The stats info section
 *
 * @param modifier The modifier to apply to the component
 * @param header The representative header of the stats section
 * @param info The info value of the statistic
 */
@Composable
private fun StatInfo(
    modifier: Modifier = Modifier,
    header: StringResource,
    info: String
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(header),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontFamily = displayFontFamily
        )
        Text(
            text = info,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}