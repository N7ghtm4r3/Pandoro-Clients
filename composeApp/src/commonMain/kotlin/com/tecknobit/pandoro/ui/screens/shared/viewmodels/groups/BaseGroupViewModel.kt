package com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

/**
 * The [BaseGroupViewModel] serves as a base view model for managing group-related operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 */
@Structure
abstract class BaseGroupViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_group` state flow holds the group data
     */
    protected val _group = MutableStateFlow<Group?>(
        value = null
    )
    val group: StateFlow<Group?> = _group

    /**
     * Method to retrieve the data of a [Group]
     */
    abstract fun retrieveGroup()

    /**
     * The [GroupDeleter] interface allows to handle the group deletion request
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    interface GroupDeleter {

        /**
         * `requestsScope` coroutine used to send the requests to the backend
         */
        val requestsScope: CoroutineScope

        /**
         * Method to delete a group
         *
         * @param group The group to delete
         * @param onDelete The action to execute when the project has been deleted
         * @param onFailure The action to execute when the deletion fails
         */
        fun deleteGroup(
            group: Group,
            onDelete: () -> Unit,
            onFailure: (JsonObject) -> Unit
        ) {
            requestsScope.launch {
                requester.sendRequest(
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

}