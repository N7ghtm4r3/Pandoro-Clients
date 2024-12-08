package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class BaseGroupViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _group = MutableStateFlow<Group?>(
        value = null
    )
    val group: StateFlow<Group?> = _group

    abstract fun retrieveGroup()

}