package com.tecknobit.pandoro.ui.screens.notes.data

import com.tecknobit.equinoxcore.helpers.CREATION_DATE_KEY
import com.tecknobit.equinoxcore.time.TimeFormatter.daysUntil
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_BY_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_DATE_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [Note] data class allow to represent a note data
 *
 * @property id The identifier of the note
 * @property author The author of the note
 * @property content The content of the note
 * @property creationDate The date when the note has been created
 * @property markedAsDone Whether the note has been completed
 * @property markedAsDoneBy The user who completed the note
 * @property markAsDoneDate When the note has been completed
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class Note(
    val id: String,
    val author: PandoroUser,
    @SerialName(CONTENT_NOTE_KEY)
    val content: String,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    @SerialName(MARKED_AS_DONE_KEY)
    val markedAsDone: Boolean = false,
    @SerialName(MARKED_AS_DONE_BY_KEY)
    val markedAsDoneBy: PandoroUser? = null,
    @SerialName(MARKED_AS_DONE_DATE_KEY)
    val markAsDoneDate: Long = -1,
) {

    /**
     * Method to calculated the days interval used to complete the note
     *
     * @return the days interval used to complete the note as [Int]
     */
    fun completionDays() : Int {
        val completionDays = creationDate.daysUntil(
            untilDate = markAsDoneDate
        ) + 1
        return completionDays.toInt()
    }

}
