@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.overview.presenter

import ChartLine
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.overview.components.OverviewCard
import com.tecknobit.pandoro.ui.screens.overview.components.ProjectsStatsSheet
import com.tecknobit.pandoro.ui.screens.overview.components.UpdatesStatsSheet
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.presentation.OverviewScreenViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.average_development_days
import pandoro.composeapp.generated.resources.development_days
import pandoro.composeapp.generated.resources.no_data_available
import pandoro.composeapp.generated.resources.overview
import pandoro.composeapp.generated.resources.projects
import pandoro.composeapp.generated.resources.retry_to_reconnect
import pandoro.composeapp.generated.resources.updates

class OverviewScreen : PandoroScreen<OverviewScreenViewModel>(
    viewModel = OverviewScreenViewModel()
) {

    private lateinit var overview: State<Overview?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = Res.string.overview
                    ) {
                        if(overview.value == null)
                            NoOverviewDataAvailable()
                        else
                            OverviewData()
                    }
                }
            },
            serverOfflineRetryText = stringResource(Res.string.retry_to_reconnect),
            serverOfflineRetryAction = { viewModel!!.retrieveOverview() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun NoOverviewDataAvailable() {
        EmptyListUI(
            icon = ChartLine,
            subText = Res.string.no_data_available,
            textStyle = TextStyle(
                fontFamily = bodyFontFamily
            ),
            themeColor = MaterialTheme.colorScheme.inversePrimary
        )
    }

    @Composable
    @NonRestartableComposable
    private fun OverviewData() {
        val widthSizeClass = getCurrentWidthSizeClass()
        when(widthSizeClass) {
            Expanded -> {  }
            else -> { OverviewColumned() }
        }
    }

    @Composable
    @NonRestartableComposable
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
                overviewStats = overview.value!!.averageDevelopmentDays
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectsStats() {
        val state = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        OverviewCard(
            title = Res.string.projects,
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

    @Composable
    @NonRestartableComposable
    private fun UpdatesStats() {
        val state = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        val scope = rememberCoroutineScope()
        OverviewCard(
            title = Res.string.updates,
            overviewStats = overview.value!!.totalUpdates,
            action = {
                scope.launch {
                    state.show()
                }
            }
        )
        UpdatesStatsSheet(
            state = state,
            scope = scope
        )
    }

    override fun onCreate() {
        viewModel!!.setActiveContext(HomeScreen::class.java)
        viewModel!!.retrieveOverview()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        overview = viewModel!!.overview.collectAsState()
    }

}