package com.tecknobit.pandoro.ui.screens.notes.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.pandoro.commondata.PandoroUser
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_BY_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_DATE_KEY
import com.tecknobit.pandorocore.MARKED_AS_DONE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Note(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(AUTHOR_KEY)
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
)
