package com.tecknobit.pandoro.ui.screens.notes.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlin.random.Random

class NotesScreenViewModel: EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var selectToDoNotes: MutableState<Boolean>

    lateinit var selectCompletedNotes: MutableState<Boolean>

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
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                        "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                        "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                markedAsDone = false
            ),
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
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean venenatis pulvinar ullamcorper. Aliquam finibus nisi eget enim elementum, quis volutpat risus semper. Praesent mauris velit, lacinia non aliquet ac, faucibus at magna. Ut nec leo elit. In eleifend hendrerit diam, nec rutrum nisi porttitor sed. Sed auctor, massa sed viverra porta, odio erat feugiat tortor, non posuere nulla velit sed lacus. Nam bibendum orci id felis hendrerit, nec suscipit nibh ultricies.\n" +
                        "Donec rutrum aliquet pellentesque. Aenean condimentum venenatis eros a tincidunt. Vivamus rhoncus mauris eget nibh ornare, ut fermentum magna pellentesque. Nulla ac turpis fermentum, ullamcorper lorem in, feugiat lorem. Nam suscipit risus nec neque malesuada, sit amet tempor nisi lobortis. Nulla maximus nisl turpis, ut commodo dui varius a. Mauris bibendum leo eu lorem viverra, in pellentesque neque sagittis. Integer lacinia vestibulum diam quis faucibus. Curabitur dui libero, facilisis vel tristique non, accumsan id augue. Quisque at faucibus tortor. Fusce maximus ante at sapien ultrices bibendum quis quis tortor. In tristique lorem ut nibh lobortis dictum.\n" +
                        "Sed a tempus ligula. Ut pretium lobortis odio, sit amet fermentum felis. Phasellus porttitor lorem eget orci pharetra, in tempor urna aliquam. Nullam feugiat ante felis, sit amet tincidunt massa venenatis et. Proin tincidunt eget nisi ut tempus. Vestibulum lectus tellus, cursus vitae dui nec, egestas gravida mauris. Mauris pharetra accumsan consequat.",
                markedAsDone = true,
                markAsDoneDate = System.currentTimeMillis()
            )
        )
        notesState.appendPage(
            items = notes, // TODO: USE THE REAL VALUE
            nextPageKey = page + 1, // TODO: USE THE REAL VALUE
            isLastPage = Random.nextBoolean()  // TODO: USE THE REAL VALUE
        )
    }

    fun manageToDoNotesFilter() {
        selectToDoNotes.value = !selectToDoNotes.value
        notesState.refresh()
    }

    fun manageCompletedNotesFilter() {
        selectCompletedNotes.value = !selectCompletedNotes.value
        notesState.refresh()
    }

    fun manageNoteStatus(
        note: Note
    ) {
        // TODO: MAKE THE REQUEST THEN
        notesState.refresh()
    }

    fun deleteNote(
        note: Note,
        onDelete: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onDelete.invoke()
    }

}