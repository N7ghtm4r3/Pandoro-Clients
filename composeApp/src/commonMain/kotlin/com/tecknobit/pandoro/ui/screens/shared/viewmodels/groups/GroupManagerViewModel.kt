package com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseArrayData
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseContent
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandorocore.enums.Role
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.random.Random

@Structure
abstract class GroupManagerViewModel : BaseGroupViewModel() {

    val candidateProjects: SnapshotStateList<String> = mutableStateListOf()

    val groupProjects: SnapshotStateList<Project> = mutableStateListOf()

    val userProjects: MutableList<Project> = mutableListOf()

    val candidateMembersState = PaginationState<Int, GroupMember>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadCandidateMembersState(
                page = page
            )
        }
    )

    lateinit var candidateMembersAvailable: MutableState<Boolean>

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
        val list = retrieveCandidateMembers(
            page = page
        )
        candidateMembersState.appendPage(
            items = if(Random.nextBoolean())
                list
            else
                emptyList(), // TODO: USE THE REAL VALUE
            nextPageKey = page + 1, // TODO: USE THE REAL VALUE
            isLastPage = Random.nextBoolean()  // TODO: USE THE REAL VALUE
        )
    }

    fun retrieveCandidateMembers(
        page: Int = PaginatedResponse.DEFAULT_PAGE
    ) : List<GroupMember> {
        // TODO: MAKE THE REQUEST THEN
        val list = listOf(
            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),

            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.ADMIN
            ),
            GroupMember(
                id = Random.nextLong().toString(),
                profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                "name",
                "surname",
                email = "name.surname@gmail.com",
                role = Role.DEVELOPER
            )
        )
        return list
    }

    fun manageProjectCandidate(
        project: Project,
        added: Boolean
    ) {
        if(added) {
            groupProjects.add(project)
            candidateProjects.add(project.id)
        } else {
            groupProjects.remove(project)
            candidateProjects.remove(project.id)
        }
    }

}