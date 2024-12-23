package com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.JsonObject

@Structure
abstract class BaseGroupViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _group = MutableStateFlow<Group?>(
        value = null
    )
    val group: StateFlow<Group?> = _group

    abstract fun retrieveGroup()

    interface GroupDeleter {

        fun deleteGroup(
            group: Group,
            onDelete: () -> Unit,
            onFailure: (JsonObject) -> Unit
        ) {
            requester.sendWRequest(
                request = {
                    deleteGroup(
                        groupId = group.id
                    )
                },
                onSuccess = { onDelete.invoke() },
                onFailure = onFailure
            )
        }

    }

}