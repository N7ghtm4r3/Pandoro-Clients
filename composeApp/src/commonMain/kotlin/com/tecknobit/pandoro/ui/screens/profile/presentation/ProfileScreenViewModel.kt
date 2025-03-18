package com.tecknobit.pandoro.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.profile.data.Changelog
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

/**
 * The **ProfileScreenViewModel** class is the support class used to change the user account settings
 * or preferences
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel

 * @see Retriever.RetrieverWrapper
 * @see EquinoxViewModel
 * @see EquinoxProfileViewModel
 */
class ProfileScreenViewModel: EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * `changelogsState` -> the state used to manage the pagination for the
     * [retrieveChangelogs] method
     */
    val changelogsState = PaginationState<Int, Changelog>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveChangelogs(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the changelogs owned by the [com.tecknobit.pandoro.localUser]
     *
     * @param page The page to request to the server
     */
    private fun retrieveChangelogs(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
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
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to read a changelog
     *
     * @param changelog The changelog to read
     */
    fun readChangelog(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    readChangelog(
                        changelogId = changelog.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to accept a [com.tecknobit.pandoro.ui.screens.groups.data.Group] invitation
     *
     * @param changelog The changelog from fetch the group
     */
    fun acceptInvite(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    acceptInvitation(
                        changelogId = changelog.id,
                        groupId = changelog.group!!.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to decline a [com.tecknobit.pandoro.ui.screens.groups.data.Group] invitation
     *
     * @param changelog The changelog from fetch the group
     */
    fun declineInvite(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    declineInvitation(
                        changelogId = changelog.id,
                        groupId = changelog.group!!.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to delete a changelog
     *
     * @param changelog The changelog to delete
     */
    fun deleteChangelog(
        changelog: Changelog
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteChangelog(
                        changelogId = changelog.id,
                        groupId = changelog.group?.id
                    )
                },
                onSuccess = {
                    changelogsState.refresh()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}