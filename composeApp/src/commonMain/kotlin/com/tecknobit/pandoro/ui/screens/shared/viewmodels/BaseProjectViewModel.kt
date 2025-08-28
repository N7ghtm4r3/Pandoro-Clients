package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.session.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject

/**
 * The [BaseProjectViewModel] serves as a base view model for managing project-related operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 */
@Structure
abstract class BaseProjectViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_project` state flow holds the project data
     */
    protected val _project = MutableStateFlow<Project?>(
        value = null
    )
    val project: StateFlow<Project?> = _project

    /**
     * Method to retrieve the data of a [Project]
     */
    abstract fun retrieveProject()

    /**
     * The [ProjectDeleter] interface allows to handle the project deletion request
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    interface ProjectDeleter {

        /**
         * `requestsScope` coroutine used to send the requests to the backend
         */
        val requestsScope: CoroutineScope

        /**
         * Method to delete a project
         *
         * @param project The project to delete
         * @param onDelete The action to execute when the project has been deleted
         * @param onFailure The action to execute when the deletion fails
         */
        fun deleteProject(
            project: Project,
            onDelete: () -> Unit,
            onFailure: (JsonObject) -> Unit
        ) {
            requestsScope.launch {
                requester.sendRequest(
                    request = {
                        deleteProject(
                            projectId = project.id
                        )
                    },
                    onSuccess = { onDelete.invoke() },
                    onFailure = { onFailure.invoke(it) }
                )
            }
        }

    }

}