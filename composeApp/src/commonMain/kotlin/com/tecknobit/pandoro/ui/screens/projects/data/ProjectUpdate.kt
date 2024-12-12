package com.tecknobit.pandoro.ui.screens.projects.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.pandoro.helpers.TimeFormatter.daysUntilNow
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandoro.ui.theme.Yellow
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

    companion object {

        @Composable
        fun UpdateStatus.toColor(): androidx.compose.ui.graphics.Color {
            return when(this) {
                UpdateStatus.SCHEDULED -> MaterialTheme.colorScheme.error
                UpdateStatus.IN_DEVELOPMENT -> Yellow()
                UpdateStatus.PUBLISHED -> Green()
            }
        }

        fun UpdateStatus.asText(): String {
            return this.name.lowercase().capitalize().replace("_", " ")
        }

    }

    fun allChangeNotesCompleted() : Boolean {
        val changeNotesCount = notes.size
        val changeNotesDone = notes.filter { changeNote-> changeNote.markedAsDone }.size
        return changeNotesCount == changeNotesDone
    }

    fun developmentDays() : Int {
        return startDate.daysUntilNow()
    }

}
