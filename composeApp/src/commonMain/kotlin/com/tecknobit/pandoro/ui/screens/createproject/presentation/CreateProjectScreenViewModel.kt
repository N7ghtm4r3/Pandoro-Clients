package com.tecknobit.pandoro.ui.screens.createproject.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.pandoro.helpers.KReviewer
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectName
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidRepository
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [CreateProjectScreenViewModel] provides the methods for the creation or the editing of a
 * [com.tecknobit.pandoro.ui.screens.projects.data.Project] item
 *
 * @param projectId The identifier of the project to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see BaseGroupViewModel
 */
class CreateProjectScreenViewModel(
    val projectId: String?
) : BaseProjectViewModel() {

    /**
     * `candidateGroups` the list of the candidates groups where share the project
     */
    val candidateGroups: SnapshotStateList<String> = mutableStateListOf()

    /**
     * `projectGroups` the list of the current groups where the project is shared
     */
    val projectGroups: SnapshotStateList<Group> = mutableStateListOf()

    /**
     * `authoredGroups` the list of the groups owned by the [com.tecknobit.pandoro.localUser]
     */
    val authoredGroups: MutableList<Group> = mutableListOf()

    /**
     * `projectIcon` the value of the icon of the project
     */
    lateinit var projectIcon: MutableState<String?>

    /**
     * `projectIconPayload` the payload of the project icon to set
     */
    var projectIconPayload: PlatformFile? = null

    /**
     * `projectName` the value of the name of the project
     */
    lateinit var projectName: MutableState<String>

    /**
     * `projectNameError` whether the [projectName] field is not valid
     */
    lateinit var projectNameError: MutableState<Boolean>

    /**
     * `projectVersion` the value of the version of the project
     */
    lateinit var projectVersion: MutableState<String>

    /**
     * `projectVersionError` whether the [projectVersion] field is not valid
     */
    lateinit var projectVersionError: MutableState<Boolean>

    /**
     * `projectRepository` the value of the repository of the project
     */
    lateinit var projectRepository: MutableState<String>

    /**
     * `projectRepositoryError` whether the [projectRepository] field is not valid
     */
    lateinit var projectRepositoryError: MutableState<Boolean>

    /**
     * `projectDescription` the value of the description of the project
     */
    lateinit var projectDescription: MutableState<String>

    /**
     * `projectDescriptionError` whether the [projectDescription] field is not valid
     */
    lateinit var projectDescriptionError: MutableState<Boolean>

    /**
     * Method to retrieve the data of a [Project] if needed
     */
    override fun retrieveProject() {
        if(projectId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getProject(
                        projectId = projectId
                    )
                },
                onSuccess = {
                    _project.value = Json.decodeFromJsonElement(it.toResponseData())
                    projectGroups.addAll(_project.value!!.groups)
                    candidateGroups.addAll(projectGroups.map { group -> group.id })
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to retrieve the current authored groups owned by the user
     */
    fun retrieveAuthoredGroups() {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getAuthoredGroups(
                        pageSize = Int.MAX_VALUE
                    )
                },
                serializer = Group.serializer(),
                onSuccess = { paginatedResponse ->
                    authoredGroups.addAll(paginatedResponse.data)
                },
                onFailure = {
                    showSnackbarMessage(it)
                }
            )
        }
    }

    /**
     * Method to manage a candidate project in the [projectGroups] and [candidateGroups] lists
     *
     * @param group The group to manage
     */
    fun manageCandidateGroup(
        group: Group
    ) {
        val groupId = group.id
        if(candidateGroups.contains(groupId)) {
            candidateGroups.remove(groupId)
            projectGroups.remove(group)
        } else {
            candidateGroups.add(groupId)
            projectGroups.add(group)
        }
    }

    /**
     * Method to create or editing the [project] invoking the correct method to execute the request
     * to handle that operation
     */
    fun workOnProject() {
        if(!isFormValid())
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    workOnProject(
                        icon = projectIconPayload?.readBytes(),
                        iconName = projectIconPayload?.name,
                        projectId = projectId,
                        name = projectName.value,
                        projectDescription = projectDescription.value,
                        projectVersion = projectVersion.value,
                        groups = candidateGroups,
                        projectRepository = projectRepository.value
                    )
                },
                onSuccess = {
                    val reviewer = KReviewer()
                    reviewer.reviewInApp {
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to validate the form data
     *
     * @return whether the form data are valid as [Boolean]
     */
    private fun isFormValid() : Boolean {
        if(!isValidProjectName(projectName.value)) {
            projectNameError.value = true
            return false
        }
        if(!isValidVersion(projectVersion.value)) {
            projectVersionError.value = true
            return false
        }
        if(!isValidRepository(projectRepository.value)) {
            projectRepositoryError.value = true
            return false
        }
        if(!isValidProjectDescription(projectDescription.value)) {
            projectDescriptionError.value = true
            return false
        }
        return true
    }

}