@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.data.ProjectPerformanceStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.group
import pandoro.composeapp.generated.resources.performance
import pandoro.composeapp.generated.resources.personal
import pandoro.composeapp.generated.resources.updates_status

/**
 * The [ModalBottomSheet] customized to represent the statistics about a project
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param overview The overview details to represent
 */
@Composable
fun ProjectsStatsSheet(
    state: SheetState,
    scope: CoroutineScope,
    overview: Overview
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 5.dp
                        ),
                    text = stringResource(Res.string.performance),
                    textAlign = TextAlign.Center,
                    fontFamily = displayFontFamily,
                    fontSize = 20.sp
                )
                HorizontalDivider()
                overview.bestPersonalPerformanceProject?.let { bestProject ->
                    StatsSection(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            )
                            .padding(
                                top = 16.dp,
                                bottom = 10.dp
                            ),
                        header = Res.string.personal,
                        bestProject = bestProject,
                        worstProject = overview.worstPersonalPerformanceProject
                    )
                }
                overview.bestGroupPerformanceProject?.let { bestProject ->
                    StatsSection(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp
                            )
                            .padding(
                                bottom = 16.dp
                            ),
                        header = Res.string.group,
                        bestProject = bestProject,
                        worstProject = overview.worstGroupPerformanceProject
                    )
                }
            }
        }
    }
}

/**
 * The statistics section to represent the best and the worst performance about projects
 *
 * @param modifier The modifier to apply to the section
 * @param header The representative header of the section
 * @param bestProject The best performance project
 * @param worstProject The worst performance project
 */
@Composable
private fun StatsSection(
    modifier: Modifier = Modifier,
    header: StringResource,
    bestProject: ProjectPerformanceStats,
    worstProject: ProjectPerformanceStats?
) {
    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(header),
            fontFamily = displayFontFamily,
            fontSize = 18.sp
        )
        ProjectPerformanceCard(
            project = bestProject,
            isTheBest = true
        )
        worstProject?.let { project ->
            ProjectPerformanceCard(
                project = project,
                isTheBest = false
            )
        }
    }
}

/**
 * The [ModalBottomSheet] customized to represent the statistics about an update
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param overview The overview details to represent
 */
@Composable
@NonRestartableComposable
fun UpdatesStatsSheet(
    state: SheetState,
    scope: CoroutineScope,
    overview: Overview
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
                    .fillMaxWidth()
                    .padding(
                        bottom = 5.dp
                    ),
                text = stringResource(Res.string.updates_status),
                textAlign = TextAlign.Center,
                fontFamily = displayFontFamily,
                fontSize = 20.sp
            )
            HorizontalDivider()
            LazyColumn (
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(overview.updatesAsIterable()) { update ->
                    UpdateOverviewCard(
                        overviewStats = update
                    )
                }
            }
        }
    }
}