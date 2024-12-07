package com.tecknobit.pandoro.ui.screens.groups.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.launch
import kotlin.random.Random

class GroupsScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val groupsState = PaginationState<Int, Group>(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveGroups(
                page = page
            )
        }
    )

    private fun retrieveGroups(
        page: Int
    ) {
        viewModelScope.launch {
            // TODO: MAKE THE REQUEST THEN
            val listFromServer = listOf<Group>()
            groupsState.appendPage(
                items = listFromServer, // TODO: USE THE REAL VALUE
                nextPageKey = page + 1, // TODO: USE THE REAL VALUE
                isLastPage = Random.nextBoolean() // TODO: USE THE REAL VALUE
            )
        }
    }

}