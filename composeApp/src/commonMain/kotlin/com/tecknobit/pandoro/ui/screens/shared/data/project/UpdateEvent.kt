package com.tecknobit.pandoro.ui.screens.shared.data.project

import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.EXTRA_CONTENT_KEY
import com.tecknobit.pandorocore.enums.events.UpdateEventType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [UpdateEvent] data class allows to represent an event occurred during the lifecycle of an update
 *
 * @property id The identifier of the event
 * @property type The type of the occurred event
 * @property author The user who made the action which created the event
 * @property timestamp When the event occurred
 * @property noteContent The content of the note if it is an event related to a change note
 * @property extraContent Extra content used when it is necessary add extra information such update version, etc...
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @since 1.2.0
 */
@Serializable
data class UpdateEvent(
    val id: String,
    val type: UpdateEventType,
    val author: PandoroUser? = null,
    val timestamp: Long,
    @SerialName(CONTENT_NOTE_KEY)
    val noteContent: String? = null,
    @SerialName(EXTRA_CONTENT_KEY)
    val extraContent: String? = null
)