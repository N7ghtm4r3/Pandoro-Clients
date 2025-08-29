package com.tecknobit.pandoro.ui.screens.shared.presentation.groups

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseContent
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.shared.data.project.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The [GroupManagerViewModel] serves as a base view model for managing group creation or editing
 * related operations
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see BaseGroupViewModel
 */
@Structure
abstract class GroupManagerViewModel : BaseGroupViewModel() {

    /**
     * `_candidatesMemberAvailable` state flow holds the the availability of the members candidate
     * to join in the group
     */
    private val _candidatesMemberAvailable = MutableSharedFlow<Boolean>(
        replay = 1
    )
    var candidatesMemberAvailable = _candidatesMemberAvailable.asSharedFlow()

    /**
     * `candidateProjects` the list of the candidates projects to share in the group
     */
    val candidateProjects: SnapshotStateList<String> = mutableStateListOf()

    /**
     * `groupProjects` the list of the current projects shared in the group
     */
    val groupProjects: SnapshotStateList<Project> = mutableStateListOf()

    /**
     * `userProjects` the list of the projects owned by the [com.tecknobit.pandoro.localUser]
     */
    val userProjects: MutableList<Project> = mutableListOf()

    /**
     * `candidateMembersState` the state used to manage the pagination for the
     * [loadCandidateMembers] method
     */
    val candidateMembersState = PaginationState<Int, GroupMember>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCandidateMembers(
                page = page
            )
        }
    )

    /**
     * `groupMembers` the list of the current members in the group
     */
    val groupMembers: SnapshotStateList<GroupMember> = mutableStateListOf()

    /**
     * Method to set the no-availability of the candidates member
     */
    fun noCandidatesAvailable() {
        viewModelScope.launch {
            _candidatesMemberAvailable.emit(false)
        }
    }

    /**
     * Method to set the availability of the candidates member
     */
    fun candidatesAvailable() {
        viewModelScope.launch {
            _candidatesMemberAvailable.emit(true)
        }
    }

    /**
     * Method to retrieve the current projects owned by the [com.tecknobit.pandoro.localUser]
     */
    fun retrieveUserProjects() {
        viewModelScope.launch {
            requester.sendRequest(
                request = { getAuthoredProjects() },
                onSuccess = {
                    val projects: List<Project> = Json.decodeFromJsonElement(it.toResponseArrayData())
                    userProjects.addAll(projects)
                    group.value?.let { group ->
                        group.projects.forEach { project ->
                            if(userProjects.any { userProject -> userProject.id == project.id })
                                candidateProjects.add(project.id)
                        }
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to load candidate members
     *
     * @param page The page to request to the server
     */
    private fun loadCandidateMembers(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getCandidateMembers(
                        page = page,
                        membersToExclude = if(_group.value != null)
                            _group.value!!.members.map { member -> member.id }
                        else
                            groupMembers.map { member -> member.id }
                    )
                },
                serializer = GroupMember.serializer(),
                onSuccess = { paginatedResponse ->
                    candidateMembersState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to count the total amount of the candidates members
     *
     * @param membersEdited The number of the members edited to include in the count
     */
    fun countCandidatesMember(
        membersEdited: Int = 0
    ) {
        viewModelScope.launch {
            val membersToExclude = (if(_group.value != null)
                _group.value!!.members
            else
                groupMembers).size + membersEdited
            requester.sendRequest(
                request = {
                    countCandidatesMember(
                        membersToExclude = membersToExclude
                    )
                },
                onSuccess = { response ->
                    val count = response.toResponseContent().toLong()
                    viewModelScope.launch {
                        delay(100)
                        _candidatesMemberAvailable.emit(count > 0)
                    }
                },
                onFailure = {
                    viewModelScope.launch {
                        _candidatesMemberAvailable.emit(false)
                    }
                }
            )
        }
    }

    /**
     * Method to manage a candidate project in the [groupProjects] and [candidateProjects] lists
     *
     * @param project The project to manage
     */
    fun manageProjectCandidate(
        project: Project
    ) {
        val projectId = project.id
        if(candidateProjects.contains(projectId)) {
            groupProjects.remove(project)
            candidateProjects.remove(projectId)
        } else {
            groupProjects.add(project)
            candidateProjects.add(projectId)
        }
    }

}