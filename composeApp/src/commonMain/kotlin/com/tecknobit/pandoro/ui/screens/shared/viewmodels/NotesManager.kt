package com.tecknobit.pandoro.ui.screens.shared.viewmodels

import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate

interface NotesManager {

    fun manageNoteStatus(
        update: ProjectUpdate? = null,
        note: Note
    )

    fun deleteNote(
        update: ProjectUpdate? = null,
        note: Note,
        onDelete: () -> Unit
    )

}