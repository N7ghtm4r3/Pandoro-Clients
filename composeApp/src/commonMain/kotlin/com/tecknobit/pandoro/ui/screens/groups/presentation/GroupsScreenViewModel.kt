package com.tecknobit.pandoro.ui.screens.groups.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.requester
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.BaseGroupViewModel.GroupDeleter
import com.tecknobit.pandorocore.enums.Role
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * The [GroupsScreenViewModel] is useful to manage the lists displayed in the
 * [com.tecknobit.pandoro.ui.screens.groups.presenter.GroupsScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxViewModel
 * @see MultipleListViewModel
 * @see GroupDeleter
 */
class GroupsScreenViewModel : MultipleListViewModel(), GroupDeleter {

    /**
     * `requestsScope` coroutine used to send the requests to the backend
     */
    override val requestsScope: CoroutineScope = MainScope()

    /**
     * `myGroupsStateFilters` the filters to apply to the [myGroupsState] list
     */
    lateinit var myGroupsStateFilters: MutableState<String>

    /**
     * `myGroupsState` the state used to manage the pagination for the
     * [retrieveMyGroups] method
     */
    val myGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveMyGroups(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the groups owned by the [com.tecknobit.pandoro.localUser]
     *
     * @param page The page to request to the server
     */
    private fun retrieveMyGroups(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
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

    /**
     * `allGroupsStateFilters` the filters to apply to the [allGroupsState] list
     */
    lateinit var allGroupsStateFilters: MutableState<String>

    /**
     * `roleFilters` the roles used as filters to apply to the [allGroupsState] list
     */
    val roleFilters: SnapshotStateList<Role> = Role.entries.toMutableStateList()

    /**
     * `allGroupsState` the state used to manage the pagination for the
     * [retrieveAllGroups] method
     */
    val allGroupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveAllGroups(
                page = page
            )
        }
    )

    /**
     * Method to retrieve the groups where the [com.tecknobit.pandoro.localUser] is a member
     *
     * @param page The page to request to the server
     */
    private fun retrieveAllGroups(
        page: Int
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
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

    /**
     * Method to retry the retrieving of the lists data
     */
    override fun retryRetrieveLists() {
        myGroupsState.retryLastFailedRequest()
        allGroupsState.retryLastFailedRequest()
    }

    /**
     * Method to check whether the filters have been set
     *
     * @param allItems Whether check the all items list
     */
    override fun areFiltersSet(
        allItems: Boolean
    ): Boolean {
        return if(allItems) {
            allGroupsStateFilters.value.isNotEmpty() || roleFilters.size != Role.entries.size
        } else
            myGroupsStateFilters.value.isNotEmpty()
    }

    /**
     * Method to manage the [roleFilters] list
     *
     * @param role The role to add or remove from the roles list
     */
    fun manageRoleFilter(
        role: Role
    ) {
        if(roleFilters.contains(role))
            roleFilters.remove(role)
        else
            roleFilters.add(role)
    }

    /**
     * Method to clear filters have been set
     *
     * @param allItems Whether clear the all items filters
     */
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

    /**
     * Method to reset as default the [roleFilters] list
     */
    fun resetRoles() {
        roleFilters.clear()
        roleFilters.addAll(Role.entries)
    }

    /**
     * Method to filter the items list
     *
     * @param allItems Whether filter the all items list
     * @param filters The filters to use
     * @param onFiltersSet The action to execute when the filters have been set
     */
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

    /**
     * Method to refresh the [myGroupsState] and the [allGroupsState] after a group deleted
     */
    fun refreshListsAfterDeletion() {
        myGroupsState.refresh()
        allGroupsState.refresh()
    }

}