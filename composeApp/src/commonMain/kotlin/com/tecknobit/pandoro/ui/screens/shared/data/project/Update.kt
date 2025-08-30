package com.tecknobit.pandoro.ui.screens.shared.data.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.equinoxcore.time.TimeFormatter.daysUntil
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.theme.green
import com.tecknobit.pandoro.ui.theme.yellow
import com.tecknobit.pandorocore.UPDATE_CREATE_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_PUBLISHED_BY_KEY
import com.tecknobit.pandorocore.UPDATE_PUBLISH_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_STARTED_BY_KEY
import com.tecknobit.pandorocore.UPDATE_START_DATE_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import com.tecknobit.pandorocore.enums.UpdateStatus
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [Update] data class allow to represent a update data
 *
 * @property id The identifier of the update
 * @property author The author of update project
 * @property creationDate The date when the update has been scheduled
 * @property targetVersion The version to reach with this update
 * @property status The current status of the update
 * @property startedBy The user who start the update
 * @property startDate The date when the update has been started
 * @property publishedBy The user who publish the update
 * @property publishDate The date when the update has been published
 * @property notes The attached change notes list
 * @property events The events occurred during the lifecycle of the update
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class Update(
    val id: String,
    val author: GroupMember,
    @SerialName(UPDATE_CREATE_DATE_KEY)
    val creationDate: Long,
    @SerialName(UPDATE_TARGET_VERSION_KEY)
    val targetVersion: String,
    val status: UpdateStatus,
    @SerialName(UPDATE_STARTED_BY_KEY)
    val startedBy: GroupMember? = null,
    @SerialName(UPDATE_START_DATE_KEY)
    val startDate: Long = -1,
    @SerialName(UPDATE_PUBLISHED_BY_KEY)
    val publishedBy: GroupMember? = null,
    @SerialName(UPDATE_PUBLISH_DATE_KEY)
    val publishDate: Long = -1,
    val notes: List<Note>,
    val events: List<UpdateEvent>
) {

    companion object {

        /**
         * Method to get the related color based on the [UpdateStatus] value
         *
         * @return the related color as [Color]
         */
        @Composable
        fun UpdateStatus.toColor(): Color {
            return when(this) {
                UpdateStatus.SCHEDULED -> MaterialTheme.colorScheme.error
                UpdateStatus.IN_DEVELOPMENT -> yellow()
                PUBLISHED -> green()
            }
        }

        /**
         * Method to format the [UpdateStatus] value as text
         *
         * @return the text value as [String]
         */
        fun UpdateStatus.asText(): String {
            return this.name.lowercase().capitalize().replace("_", " ")
        }

    }

    /**
     * Method to check whether all the [notes] attached are completed
     *
     * @return whether all the [notes] attached are completed as [Boolean]
     */
    fun allChangeNotesCompleted() : Boolean {
        return notes.size == completedChangeNotes()
    }

    /**
     * Method to count all the completed [notes]
     *
     * @return the count all the completed [notes] as [Int]
     */
    fun completedChangeNotes(): Int {
        return notes.filter { changeNote-> changeNote.markedAsDone }.size
    }

    /**
     * Method to count the current development days spent
     *
     * @return the count the current development days spent as [Int]
     */
    fun developmentDays() : Int {
        val developmentDays = startDate.daysUntil(
            untilDate = if(status == PUBLISHED)
                publishDate
            else
                TimeFormatter.currentTimestamp()
        ) + 1
        return developmentDays.toInt()
    }

}
