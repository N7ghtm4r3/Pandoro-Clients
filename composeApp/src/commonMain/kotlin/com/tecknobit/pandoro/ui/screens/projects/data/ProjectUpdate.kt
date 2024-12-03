package com.tecknobit.pandoro.ui.screens.projects.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.pandoro.ui.commondata.PandoroUser
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.NOTES_KEY
import com.tecknobit.pandorocore.UPDATE_CREATE_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_PUBLISHED_BY_KEY
import com.tecknobit.pandorocore.UPDATE_PUBLISH_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_STARTED_BY_KEY
import com.tecknobit.pandorocore.UPDATE_START_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_STATUS_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectUpdate(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(AUTHOR_KEY)
    val author: PandoroUser,
    @SerialName(UPDATE_CREATE_DATE_KEY)
    val createDate: Long,
    @SerialName(UPDATE_TARGET_VERSION_KEY)
    val targetVersion: String,
    @SerialName(UPDATE_STATUS_KEY)
    val status: UpdateStatus,
    @SerialName(UPDATE_STARTED_BY_KEY)
    val startedBy: PandoroUser? = null,
    @SerialName(UPDATE_START_DATE_KEY)
    val startDate: Long = -1,
    @SerialName(UPDATE_PUBLISHED_BY_KEY)
    val publishedBy: PandoroUser? = null,
    @SerialName(UPDATE_PUBLISH_DATE_KEY)
    val publishDate: Long = -1,
    @SerialName(NOTES_KEY)
    val notes: List<Note>,
) {

    fun allChangeNotesCompleted() : Boolean {
        val changeNotesCount = notes.size
        val changeNotesDone = notes.filter { changeNote-> changeNote.markedAsDone }.size
        return changeNotesCount == changeNotesDone
    }

    fun developmentDays() : Int {
        return Instant.fromEpochMilliseconds(startDate)
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
            .daysUntil(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    }

}
