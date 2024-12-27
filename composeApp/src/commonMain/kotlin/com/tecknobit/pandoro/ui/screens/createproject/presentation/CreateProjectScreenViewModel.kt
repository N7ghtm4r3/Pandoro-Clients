package com.tecknobit.pandoro.ui.screens.createproject.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.BaseProjectViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectName
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidRepository
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class CreateProjectScreenViewModel(
    val projectId: String?
) : BaseProjectViewModel() {

    val candidateGroups: SnapshotStateList<String> = mutableStateListOf()

    val projectGroups: SnapshotStateList<Group> = mutableStateListOf()

    val authoredGroups: MutableList<Group> = mutableListOf()

    lateinit var projectIcon: MutableState<String?>

    lateinit var projectName: MutableState<String>

    lateinit var projectNameError: MutableState<Boolean>

    lateinit var projectVersion: MutableState<String>

    lateinit var projectVersionError: MutableState<Boolean>

    lateinit var projectRepository: MutableState<String>

    lateinit var projectRepositoryError: MutableState<Boolean>

    lateinit var projectDescription: MutableState<String>

    lateinit var projectDescriptionError: MutableState<Boolean>

    override fun retrieveProject() {
        if(projectId == null)
            return
        viewModelScope.launch {
            requester.sendWRequest(
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
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    fun retrieveAuthoredGroups() {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
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
                    showSnackbarMessage(it.toResponseContent())
                }
            )
        }
    }

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

    fun workOnProject() {
        if(!isFormValid())
            return
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    workOnProject(
                        icon = if(projectIcon.value == _project.value?.icon)
                            ""
                        else
                            projectIcon.value,
                        projectId = projectId,
                        name = projectName.value,
                        projectDescription = projectDescription.value,
                        projectVersion = projectVersion.value,
                        groups = candidateGroups,
                        projectRepository = projectRepository.value
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

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