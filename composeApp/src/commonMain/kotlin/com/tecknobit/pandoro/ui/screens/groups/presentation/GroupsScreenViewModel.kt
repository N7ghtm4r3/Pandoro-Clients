package com.tecknobit.pandoro.ui.screens.groups.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.helpers.session.setServerOfflineValue
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.helpers.PandoroRequester.Companion.sendPaginatedWRequest
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandorocore.enums.Role
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch

class GroupsScreenViewModel : MultipleListViewModel(), GroupDeleter {

    lateinit var myGroupsStateFilters: MutableState<String>

    val myGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveMyGroups(
                page = page
            )
        }
    )

    private fun retrieveMyGroups(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
                request = {
                    getAuthoredGroups(
                        page = page,
                        nameFilter = myGroupsStateFilters.value
                    )
                },
                serializer = Group.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    myGroupsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                    myGroupsState.setError(Exception())
                }
            )
        }
    }

    lateinit var allGroupsStateFilters: MutableState<String>

    val roleFilters: SnapshotStateList<Role> = Role.entries.toMutableStateList()

    val allGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveAllGroups(
                page = page
            )
        }
    )

    private fun retrieveAllGroups(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedWRequest(
                request = {
                    getGroups(
                        page = page,
                        nameFilter = allGroupsStateFilters.value,
                        roles = roleFilters
                    )
                },
                serializer = Group.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    allGroupsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                    allGroupsState.setError(Exception())
                }
            )
        }
    }

    override fun retryRetrieveLists() {
        myGroupsState.retryLastFailedRequest()
        allGroupsState.retryLastFailedRequest()
    }

    override fun areFiltersSet(
        allItems: Boolean
    ): Boolean {
        return if(allItems) {
            allGroupsStateFilters.value.isNotEmpty() || roleFilters.size != Role.entries.size
        } else
            myGroupsStateFilters.value.isNotEmpty()
    }

    fun manageRoleFilter(
        role: Role
    ) {
        if(roleFilters.contains(role))
            roleFilters.remove(role)
        else
            roleFilters.add(role)
    }

    override fun clearFilters(
        allItems: Boolean
    ) {
        if(allItems) {
            allGroupsStateFilters.value = ""
            resetRoles()
            allGroupsState.refresh()
        } else {
            myGroupsStateFilters.value = ""
            myGroupsState.refresh()
        }
    }

    fun resetRoles() {
        roleFilters.clear()
        roleFilters.addAll(Role.entries)
    }

    override fun filterItems(
        allItems: Boolean,
        filters: MutableState<String>,
        onFiltersSet: () -> Unit
    ) {
        if(allItems) {
            allGroupsStateFilters.value = filters.value
            allGroupsState.refresh()
        } else {
            myGroupsStateFilters.value = filters.value
            myGroupsState.refresh()
        }
        onFiltersSet.invoke()
    }

    fun refreshListsAfterDeletion() {
        myGroupsState.refresh()
        allGroupsState.refresh()
    }

}