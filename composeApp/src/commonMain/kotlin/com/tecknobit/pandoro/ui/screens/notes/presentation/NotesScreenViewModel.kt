package com.tecknobit.pandoro.ui.screens.notes.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.ui.commondata.PandoroUser
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class NotesScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    val notesState = PaginationState<Int, Note>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            retrieveNotes(
                page = page
            )
        }
    )

    private fun retrieveNotes(
        page: Int
    ) {
        // TODO: MAKE THE PAGINATED REQUEST THEN
        val notes = mutableListOf(
            Note(
                id = Random.nextLong().toString(),
                author = PandoroUser(
                    id = Random.nextLong().toString(),
                    profilePic = "https://res.cloudinary.com/momentum-media-group-pty-ltd/image/upload/c_fill,q_auto:best,f_auto,e_unsharp_mask:80,w_830,h_478/Space%20Connect%2Fspace-exploration-sc_fm1ysf",
                    "name",
                    "surname",
                    email = "name.surname@gmail.com"
                ),
                creationDate = System.currentTimeMillis(),
                content = "111",
                markedAsDone = Random.nextBoolean()
            )
        )
        notesState.appendPage(
            items = notes, // TODO: USE THE REAL VALUE
            nextPageKey = page + 1, // TODO: USE THE REAL VALUE
            isLastPage = Random.nextBoolean()  // TODO: USE THE REAL VALUE
        )
    }

}