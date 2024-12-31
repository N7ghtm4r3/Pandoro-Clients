package com.tecknobit.pandoro.ui.screens.group.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandorocore.enums.Role
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [GroupScreenViewModel] provides the methods to display and operate on a
 * [com.tecknobit.pandoro.ui.screens.groups.data.Group] item
 *
 * @param groupId The identifier of the group to display
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see GroupScreenViewModel
 * @see GroupDeleter
 */
class GroupScreenViewModel(
    private val groupId: String
) : GroupManagerViewModel(), GroupDeleter {

    /**
     * Method to retrieve the data of a [Group]
     */
    override fun retrieveGroup() {
        execRefreshingRoutine(
            currentContext = GroupScreen::class.java,
            routine = {
                requester.sendWRequest(
                    request = {
                        getGroup(
                            groupId = groupId
                        )
                    },
                    onSuccess = {
                        setServerOfflineValue(false)
                        _group.value = Json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = { },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            }
        )
    }

    /**
     * Method to change the role of a [member]
     *
     * @param member The member to change his/her role
     * @param role The new role to set on the [member]
     * @param onChange The action to execute when the role changed
     */
    fun changeMemberRole(
        member: GroupMember,
        role: Role,
        onChange: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    changeMemberRole(
                        groupId = groupId,
                        memberId = member.id,
                        role = role
                    )
                },
                onSuccess = { onChange.invoke() },
                onFailure = { showSnackbarMessage(it.toResponseContent())}
            )
        }
    }

    /**
     * Method to add new members to the [group]
     */
    fun addMembers() {
        viewModelScope.launch {
            val membersAdded = groupMembers.map { member -> member.id }
            requester.sendWRequest(
                request = {
                    addMembers(
                        groupId = groupId,
                        members = membersAdded
                    )
                },
                onSuccess = {
                    refreshCandidates(
                        membersEdited = membersAdded.size
                    )
                },
                onFailure = { showSnackbarMessage(it.toResponseContent())}
            )
        }
    }

    /**
     * Method to edit the current projects (owned by the [com.tecknobit.pandoro.localUser]) shared
     * with the [group]
     */
    fun editProjects() {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    editProjects(
                        groupId = groupId,
                        projects = candidateProjects
                    )
                },
                onSuccess = {
                },
                onFailure = { showSnackbarMessage(it.toResponseContent())}
            )
        }
    }

    /**
     * Method to remove a [member] from the [group]
     *
     * @param member The member to remove
     */
    fun removeMember(
        member: GroupMember
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    removeMember(
                        groupId = groupId,
                        memberId = member.id
                    )
                },
                onSuccess = {
                    refreshCandidates(
                        membersEdited = -1
                    )
                },
                onFailure = { showSnackbarMessage(it.toResponseContent()) }
            )
        }
    }

    /**
     * Method to refresh the current candidates available to be added in the [group]
     *
     * @param membersEdited The number of the members edited to include in the count
     */
    private fun refreshCandidates(
        membersEdited: Int
    ) {
        countCandidatesMember(
            membersEdited = membersEdited
        )
        groupMembers.clear()
        candidateMembersState.refresh()
    }

    /**
     * Method to leave from the [group]
     */
    fun leaveGroup() {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    leaveGroup(
                        groupId = groupId,
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it.toResponseContent())}
            )
        }
    }

}