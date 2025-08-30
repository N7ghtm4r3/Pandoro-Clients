package com.tecknobit.pandoro.ui.screens.shared.presentation

import com.tecknobit.pandoro.ui.screens.shared.data.project.Update
import com.tecknobit.pandoro.ui.screens.notes.data.Note

/**
 * The [NotesManager] interface allows to manage the [Note] data
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface NotesManager {

    /**
     * Method to manage the status of the [note]
     *
     * @param update The update owner of the [note]
     * @param note The note to manage
     */
    fun manageNoteStatus(
        update: Update? = null,
        note: Note
    )

    /**
     * Method to delete a [note]
     *
     * @param update The update owner of the [note]
     * @param note The note to delete
     * @param onDelete The action to execute when the note has been deleted
     */
    fun deleteNote(
        update: Update? = null,
        note: Note,
        onDelete: () -> Unit
    )

}