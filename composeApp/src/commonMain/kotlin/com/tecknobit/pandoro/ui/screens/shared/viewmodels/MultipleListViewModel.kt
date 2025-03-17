package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure

/**
 * The [MultipleListViewModel] is useful to manage the lists displayed in a screen such as
 * [com.tecknobit.pandoro.ui.screens.projects.presenter.ProjectsScreen] or
 * [com.tecknobit.pandoro.ui.screens.groups.presenter.GroupsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 */
@Structure
abstract class MultipleListViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * Method to retry the retrieving of the lists data
     */
    abstract fun retryRetrieveLists()

    /**
     * Method to check whether the filters have been set
     *
     * @param allItems Whether check the all items list
     */
    abstract fun areFiltersSet(
        allItems: Boolean
    ) : Boolean

    /**
     * Method to clear filters have been set
     *
     * @param allItems Whether clear the all items filters
     */
    abstract fun clearFilters(
        allItems: Boolean
    )

    /**
     * Method to filter the items list
     *
     * @param allItems Whether filter the all items list
     * @param filters The filters to use
     * @param onFiltersSet The action to execute when the filters have been set
     */
    abstract fun filterItems(
        allItems: Boolean,
        filters: MutableState<String>,
        onFiltersSet: () -> Unit
    )

}