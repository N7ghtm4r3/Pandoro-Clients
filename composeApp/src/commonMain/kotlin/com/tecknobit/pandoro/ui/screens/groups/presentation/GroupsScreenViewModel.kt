package com.tecknobit.pandoro.ui.screens.groups.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch
import kotlin.random.Random

class GroupsScreenViewModel : MultipleListViewModel() {

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
            // TODO: MAKE THE REQUEST THEN
            val listFromServer = listOf<Group>()
            myGroupsState.appendPage(
                items = listFromServer, // TODO: USE THE REAL VALUE
                nextPageKey = page + 1, // TODO: USE THE REAL VALUE
                isLastPage = Random.nextBoolean() // TODO: USE THE REAL VALUE
            )
        }
    }

    lateinit var allGroupsStateFilters: MutableState<String>

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
            // TODO: MAKE THE REQUEST THEN
            val listFromServer = listOf<Group>()
            allGroupsState.appendPage(
                items = listFromServer, // TODO: USE THE REAL VALUE
                nextPageKey = page + 1, // TODO: USE THE REAL VALUE
                isLastPage = Random.nextBoolean() // TODO: USE THE REAL VALUE
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
        return allItems
    }

    override fun clearFilters(
        allItems: Boolean
    ) {

    }

}