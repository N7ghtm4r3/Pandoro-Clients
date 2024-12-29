package com.tecknobit.pandoro.ui.screens.creategroup.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupNameValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import org.jetbrains.compose.resources.getString
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.wrong_logo

/**
 * The [CreateGroupScreenViewModel] provides the methods for the creation or the editing of a
 * [com.tecknobit.pandoro.ui.screens.groups.data.Group] item
 *
 * @param groupId The identifier of the group to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see BaseGroupViewModel
 */
class CreateGroupScreenViewModel(
    val groupId: String?
) : GroupManagerViewModel() {

    /**
     * **groupLogo** -> the value of the logo of the group
     */
    lateinit var groupLogo: MutableState<String?>

    /**
     * **groupName** -> the value of the name of the group
     */
    lateinit var groupName: MutableState<String>

    /**
     * **groupNameError** -> whether the [groupName] field is not valid
     */
    lateinit var groupNameError: MutableState<Boolean>

    /**
     * **groupDescription** -> the value of the description of the group
     */
    lateinit var groupDescription: MutableState<String>

    /**
     * **groupDescriptionError** -> whether the [groupDescription] field is not valid
     */
    lateinit var groupDescriptionError: MutableState<Boolean>

    /**
     * Method to retrieve the data of a [Group] if needed
     */
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

    /**
     * Method to create or editing the [group] invoking the correct method to execute the request
     * to handle that operation
     */
    fun workOnGroup() {
        if(!isFormValid())
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

    /**
     * Method to validate the form data
     *
     * @return whether the form data are valid as [Boolean]
     */
    private fun isFormValid() : Boolean {
        if(groupLogo.value.isNullOrEmpty()) {
            // FIXME: TO USE THE BUILT-IN ONE
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