package com.tecknobit.pandoro.ui.screens.shared.data.project

import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.CONTENT_NOTE_KEY
import com.tecknobit.pandorocore.EXTRA_CONTENT_KEY
import com.tecknobit.pandorocore.enums.events.UpdateEventType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
// TODO: TO COMMENT 1.2.0
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