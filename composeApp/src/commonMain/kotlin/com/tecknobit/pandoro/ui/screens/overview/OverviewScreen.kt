package com.tecknobit.pandoro.ui.screens.overview

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen

class OverviewScreen : EquinoxScreen<OverviewScreenViewModel>() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
    }

    override fun onCreate() {
        viewModel!!.setActiveContext(HomeScreen::class.java)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}