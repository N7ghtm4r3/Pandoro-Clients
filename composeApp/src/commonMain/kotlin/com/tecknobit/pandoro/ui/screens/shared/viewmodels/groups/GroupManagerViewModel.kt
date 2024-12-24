package com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxbackend.Requester.Companion.RESPONSE_DATA_KEY
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseArrayData
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long

@Structure
abstract class GroupManagerViewModel : BaseGroupViewModel() {

    private val _candidatesMemberAvailable = MutableStateFlow(
        value = false
    )
    var candidatesMemberAvailable: MutableStateFlow<Boolean> = _candidatesMemberAvailable

    val candidateProjects: SnapshotStateList<String> = mutableStateListOf()

    val groupProjects: SnapshotStateList<Project> = mutableStateListOf()

    val userProjects: MutableList<Project> = mutableListOf()

    val candidateMembersState = PaginationState<Int, GroupMember>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCandidateMembersState(
                page = page
            )
        }
    )

    val groupMembers: SnapshotStateList<GroupMember> = mutableStateListOf()

    fun retrieveUserProjects() {
        requester.sendWRequest(
            request = { getAuthoredProjects() },
            onSuccess = {
                val projects: List<Project> = Json.decodeFromJsonElement(it.toResponseArrayData());
                userProjects.addAll(projects)
            },
            onFailure = { showSnackbarMessage(it.toResponseContent()) }
        )
    }

    private fun loadCandidateMembersState(
        page: Int
    ) {
        requester.sendPaginatedWRequest(
            request = {
                getCandidateMembers(
                    page = page
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
            onFailure = { showSnackbarMessage(it.toResponseContent()) }
        )
    }

    fun countCandidatesMember() {
        requester.sendWRequest(
            request = {
                countCandidatesMember()
            },
            onSuccess = { response ->
                val count = response[RESPONSE_DATA_KEY]!!.jsonPrimitive.long
                _candidatesMemberAvailable.value = count > 0
            },
            onFailure = { _candidatesMemberAvailable.value = false}
        )
    }

    fun manageProjectCandidate(
        project: Project
    ) {
        val projectId = project.id
        if(candidateProjects.contains(projectId)) {
            groupProjects.removeIf { it.id == projectId }
            candidateProjects.remove(projectId)
        } else {
            groupProjects.add(project)
            candidateProjects.add(projectId)
        }
    }

}