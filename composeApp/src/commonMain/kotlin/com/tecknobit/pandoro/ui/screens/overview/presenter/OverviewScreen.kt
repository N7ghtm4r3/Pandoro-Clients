@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.pandoro.ui.screens.overview.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.ExpandedClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.*
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.overview.components.OverviewCard
import com.tecknobit.pandoro.ui.screens.overview.components.ProjectsStatsSheet
import com.tecknobit.pandoro.ui.screens.overview.components.UpdatesStatsSheet
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.presentation.OverviewScreenViewModel
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.theme.AppTypography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.average_development_days
import pandoro.composeapp.generated.resources.development_days
import pandoro.composeapp.generated.resources.general
import pandoro.composeapp.generated.resources.no_data_available
import pandoro.composeapp.generated.resources.no_overview_data
import pandoro.composeapp.generated.resources.projects
import pandoro.composeapp.generated.resources.retry_to_reconnect
import pandoro.composeapp.generated.resources.updates

/**
 * The [OverviewScreen] displays the details of an overview
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
 * @see PandoroScreen
 */
class OverviewScreen : PandoroScreen<OverviewScreenViewModel>(
    viewModel = OverviewScreenViewModel()
) {

    /**
     * `overview` state flow holds the overview data
     */
    private lateinit var overview: State<Overview?>

    @OptIn(ExperimentalComposeApi::class)
    @Composable
    override fun ColumnScope.ScreenContent() {
        SessionFlowContainer(
            modifier = Modifier
                .fillMaxSize(),
            state = rememberSessionFlowState(),
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) }
                ) {
                    if(overview.value == null)
                        NoOverviewDataAvailable()
                    else
                        OverviewData()
                }
            },
            onServerOffline = {
                TextButton(
                    onClick = {
                        viewModel.retrieveOverview()
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.retry_to_reconnect)
                    )
                }
            }
        )
    }

    /**
     * The UI to display when no overview data are available to be displayed
     */
    @Composable
    private fun NoOverviewDataAvailable() {
        EmptyState(
            containerModifier = Modifier
                .fillMaxSize(),
            resource = Res.drawable.no_overview_data,
            resourceSize = responsiveAssignment(
                onExpandedSizeClass = { 325.dp },
                onMediumSizeClass = { 275.dp },
                onCompactSizeClass = { 250.dp }
            ),
            contentDescription = "No data available",
            title = stringResource(Res.string.no_data_available),
            titleStyle = AppTypography.bodyLarge
        )
    }

    /**
     * Method to display dynamically based on the screen size the content of the overview
     */
    @Composable
    @LayoutCoordinator
    private fun OverviewData() {
        ResponsiveContent(
            onExpandedSizeClass = { DashboardOverview() },
            onMediumSizeClass = { OverviewColumned() },
            onCompactSizeClass = { OverviewColumned() }
        )
    }

    /**
     * The overview data displayed as dashboard
     */
    @Composable
    @ExpandedClassComponent
    private fun DashboardOverview() {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(
                        maxWidth = 1250.dp,
                        maxHeight = 750.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row (
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ProjectsStats(
                        modifier = Modifier
                            .weight(1f),
                        pieSize = 200.dp,
                        pieStroke = 42.dp,
                    )
                    UpdatesStats(
                        modifier = Modifier
                            .weight(1f),
                        pieSize = 200.dp,
                        pieStroke = 42.dp,
                    )
                }
                Row (
                    modifier = Modifier
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OverviewCard(
                        modifier = Modifier
                            .weight(1f),
                        title = Res.string.development_days,
                        pieSize = 200.dp,
                        pieStroke = 42.dp,
                        overviewStats = overview.value!!.developmentDays
                    )
                    OverviewCard(
                        modifier = Modifier
                            .weight(1f),
                        title = Res.string.average_development_days,
                        totalHeader = Res.string.general,
                        pieSize = 200.dp,
                        pieStroke = 42.dp,
                        overviewStats = overview.value!!.averageDevelopmentDays
                    )
                }
            }
        }
    }

    /**
     * The overview data displayed in column for the mobile devices for example
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
    )
    private fun OverviewColumned() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ProjectsStats()
            UpdatesStats()
            OverviewCard(
                title = Res.string.development_days,
                overviewStats = overview.value!!.developmentDays
            )
            OverviewCard(
                title = Res.string.average_development_days,
                totalHeader = Res.string.general,
                overviewStats = overview.value!!.averageDevelopmentDays
            )
        }
    }

    /**
     * The statistics about the projects
     *
     * @param modifier The modifier to apply to the card
     * @param pieSize The size of the pie chart
     * @param pieStroke The stroke to apply to the pie chart
     */
    @Composable
    private fun ProjectsStats(
        modifier: Modifier = Modifier,
        pieSize: Dp = 150.dp,
        pieStroke: Dp = 35.dp,
    ) {
        val state = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        OverviewCard(
            modifier = modifier,
            title = Res.string.projects,
            pieSize = pieSize,
            pieStroke = pieStroke,
            overviewStats = overview.value!!.totalProjects,
            actionIcon = Icons.Default.Speed,
            action = {
                scope.launch {
                    state.show()
                }
            }
        )
        ProjectsStatsSheet(
            state = state,
            scope = scope,
            overview = overview.value!!
        )
    }

    /**
     * The statistics about the updates
     *
     * @param modifier The modifier to apply to the card
     * @param pieSize The size of the pie chart
     * @param pieStroke The stroke to apply to the pie chart
     */
    @Composable
    private fun UpdatesStats(
        modifier: Modifier = Modifier,
        pieSize: Dp = 150.dp,
        pieStroke: Dp = 35.dp,
    ) {
        val state = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        OverviewCard(
            modifier = modifier,
            title = Res.string.updates,
            pieSize = pieSize,
            pieStroke = pieStroke,
            overviewStats = overview.value!!.totalUpdates,
            action = {
                scope.launch {
                    state.show()
                }
            }
        )
        UpdatesStatsSheet(
            state = state,
            scope = scope,
            overview = overview.value!!
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been created
     */
    override fun onCreate() {
        viewModel.setActiveContext(HomeScreen::class)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        SideEffect {
            viewModel.retrieveOverview()
        }
        overview = viewModel.overview.collectAsState()
    }

}