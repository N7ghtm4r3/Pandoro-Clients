package com.tecknobit.pandoro.ui.screens.groups.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel

class GroupsScreen : EquinoxScreen<GroupsScreenViewModel>(
    viewModel = GroupsScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}