@file:OptIn(ExperimentalPaginationApi::class)

package com.tecknobit.pandoro.ui.screens.group.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import com.tecknobit.pandorocore.enums.Role
import io.github.ahmad_hamwi.compose.pagination.ExperimentalPaginationApi
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class GroupScreenViewModel(
    private val groupId: String
) : GroupManagerViewModel(), GroupDeleter {

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
                    onFailure = { setHasBeenDisconnectedValue(true) },
                    onConnectionError = { setServerOfflineValue(true) }
                )
            }
        )
    }

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

    private fun refreshCandidates(
        membersEdited: Int
    ) {
        countCandidatesMember(
            membersEdited = membersEdited
        )
        groupMembers.clear()
        candidateMembersState.refresh()
    }

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