@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.group.presentation

import androidx.compose.runtime.ExperimentalComposeApi
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandorocore.enums.Role
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
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
     * `requestsScope` coroutine used to send the requests to the backend
     */
    override val requestsScope: CoroutineScope = MainScope()

    /**
     * `sessionFlowState` the state used to manage the session lifecycle in the screen
     */
    lateinit var sessionFlowState: SessionFlowState

    /**
     * Method to retrieve the data of a [Group]
     */
    override fun retrieveGroup() {
        retrieve(
            currentContext = GroupScreen::class,
            routine = {
                requester.sendRequest(
                    request = {
                        getGroup(
                            groupId = groupId
                        )
                    },
                    onSuccess = {
                        sessionFlowState.notifyOperational()
                        _group.value = Json.decodeFromJsonElement(it.toResponseData())
                    },
                    onFailure = {},
                    onConnectionError = { sessionFlowState.notifyServerOffline() }
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
            requester.sendRequest(
                request = {
                    changeMemberRole(
                        groupId = groupId,
                        memberId = member.id,
                        role = role
                    )
                },
                onSuccess = { onChange.invoke() },
                onFailure = { showSnackbarMessage(it)}
            )
        }
    }

    /**
     * Method to add new members to the [group]
     */
    fun addMembers() {
        viewModelScope.launch {
            val membersAdded = groupMembers.map { member -> member.id }
            requester.sendRequest(
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
                onFailure = { showSnackbarMessage(it)}
            )
        }
    }

    /**
     * Method to edit the current projects (owned by the [com.tecknobit.pandoro.localUser]) shared
     * with the [group]
     */
    fun editProjects() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    editProjects(
                        groupId = groupId,
                        projects = candidateProjects
                    )
                },
                onSuccess = {
                },
                onFailure = { showSnackbarMessage(it)}
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
            requester.sendRequest(
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
                onFailure = { showSnackbarMessage(it) }
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
            requester.sendRequest(
                request = {
                    leaveGroup(
                        groupId = groupId,
                    )
                },
                onSuccess = { navigator.goBack() },
                onFailure = { showSnackbarMessage(it)}
            )
        }
    }

}