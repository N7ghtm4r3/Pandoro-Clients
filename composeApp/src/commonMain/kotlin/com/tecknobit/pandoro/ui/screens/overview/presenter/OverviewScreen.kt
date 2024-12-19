package com.tecknobit.pandoro.ui.screens.overview.presenter

import ChartLine
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.overview.components.OverviewCard
import com.tecknobit.pandoro.ui.screens.overview.data.Overview
import com.tecknobit.pandoro.ui.screens.overview.presentation.OverviewScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.no_data_available
import pandoro.composeapp.generated.resources.overview
import pandoro.composeapp.generated.resources.retry_to_reconnect
import pandoro.composeapp.generated.resources.total_projects

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
            Compact -> { OverviewColumned() }
            else -> {

            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun OverviewColumned() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            OverviewCard(
                title = Res.string.total_projects,
                overviewStats = overview.value!!.totalUpdates
            )
        }
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