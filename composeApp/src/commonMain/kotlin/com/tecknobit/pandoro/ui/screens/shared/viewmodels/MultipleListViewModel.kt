package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
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

    abstract fun filterItems(
        allItems: Boolean,
        filters: MutableState<String>,
        onFiltersSet: () -> Unit
    )

}