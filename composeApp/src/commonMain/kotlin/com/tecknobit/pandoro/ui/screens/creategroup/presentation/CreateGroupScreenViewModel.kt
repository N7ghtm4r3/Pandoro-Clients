package com.tecknobit.pandoro.ui.screens.creategroup.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupNameValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.jetbrains.compose.resources.getString
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.wrong_logo

class CreateGroupScreenViewModel(
    val groupId: String?
) : GroupManagerViewModel() {

    lateinit var groupLogo: MutableState<String?>

    lateinit var groupName: MutableState<String>

    lateinit var groupNameError: MutableState<Boolean>

    lateinit var groupDescription: MutableState<String>

    lateinit var groupDescriptionError: MutableState<Boolean>

    override fun retrieveGroup() {
        if(groupId == null)
            return
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    getGroup(
                        groupId = groupId
                    )
                },
                onSuccess = {
                    _group.value = Json.decodeFromJsonElement(it.toResponseData())
                    groupProjects.addAll(_group.value!!.projects)
                    candidateProjects.addAll(groupProjects.map { project -> project.id })
                    groupMembers.addAll(_group.value!!.members)
                },
                onFailure = {
                    showSnackbarMessage(it.toResponseContent())
                }
            )
        }
    }

    fun workOnGroup() {
        if(!validForm())
            return
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    workOnGroup(
                        groupId = groupId,
                        logo = if(groupLogo.value == _group.value?.logo)
                            ""
                        else
                            groupLogo.value,
                        name = groupName.value,
                        description = groupDescription.value,
                        members = groupMembers,
                        projects = candidateProjects
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it.toResponseContent())}
            )
        }
    }

    private fun validForm() : Boolean {
        if(groupLogo.value.isNullOrEmpty()) {
            viewModelScope.launch {
                showSnackbarMessage(
                    message = getString(
                        resource = Res.string.wrong_logo
                    )
                )
            }
            return false
        }
        if(!isGroupNameValid(groupName.value)) {
            groupNameError.value = true
            return false
        }
        if(!isGroupDescriptionValid(groupDescription.value)) {
            groupDescriptionError.value = true
            return false
        }
        return true
    }

}