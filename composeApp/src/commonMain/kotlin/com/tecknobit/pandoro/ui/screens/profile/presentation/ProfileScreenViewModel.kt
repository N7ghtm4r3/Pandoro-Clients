package com.tecknobit.pandoro.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendWRequest
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.toResponseData
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.profile.data.Changelog
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

class ProfileScreenViewModel: EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    lateinit var profilePic: MutableState<String>

    val changelogsState = PaginationState<Int, Changelog>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveChangelogs(
                page = page
            )
        }
    )

    private fun retrieveChangelogs(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
                request = {
                    getChangelogs(
                        page = page
                    )
                },
                serializer = Changelog.serializer(),
                onSuccess = { paginatedResponse ->
                    changelogsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { showSnackbarMessage(it.toResponseData()) }
            )
        }
    }

    fun readChangelog(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    readChangelog(
                        changelogId = changelog.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it.toResponseData()) }
            )
        }
    }

    fun acceptInvite(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    acceptInvitation(
                        changelogId = changelog.id,
                        groupId = changelog.group!!.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it.toResponseData()) }
            )
        }
    }

    fun declineInvite(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    declineInvitation(
                        changelogId = changelog.id,
                        groupId = changelog.group!!.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it.toResponseData()) }
            )
        }
    }

    fun deleteChangelog(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendWRequest(
                request = {
                    deleteChangelog(
                        changelogId = changelog.id,
                        groupId = changelog.group?.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it.toResponseData()) }
            )
        }
    }

}