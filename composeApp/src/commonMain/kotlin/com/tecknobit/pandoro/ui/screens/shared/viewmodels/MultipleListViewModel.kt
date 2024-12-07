package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure

@Structure
abstract class MultipleListViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    abstract fun retryRetrieveLists()

    abstract fun areFiltersSet(
        allItems: Boolean
    ) : Boolean

    abstract fun clearFilters(
        allItems: Boolean
    )

}