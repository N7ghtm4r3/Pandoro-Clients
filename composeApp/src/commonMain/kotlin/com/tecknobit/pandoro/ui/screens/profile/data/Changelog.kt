package com.tecknobit.pandoro.ui.screens.profile.data

import androidx.compose.runtime.Composable
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandorocore.CHANGELOG_EVENT_KEY
import com.tecknobit.pandorocore.CHANGELOG_EXTRA_CONTENT_KEY
import com.tecknobit.pandorocore.CHANGELOG_READ_KEY
import com.tecknobit.pandorocore.enums.ChangelogEvent
import com.tecknobit.pandorocore.enums.ChangelogEvent.GROUP_DELETED
import com.tecknobit.pandorocore.enums.ChangelogEvent.INVITED_GROUP
import com.tecknobit.pandorocore.enums.ChangelogEvent.JOINED_GROUP
import com.tecknobit.pandorocore.enums.ChangelogEvent.LEFT_GROUP
import com.tecknobit.pandorocore.enums.ChangelogEvent.PROJECT_ADDED
import com.tecknobit.pandorocore.enums.ChangelogEvent.PROJECT_REMOVED
import com.tecknobit.pandorocore.enums.ChangelogEvent.ROLE_CHANGED
import com.tecknobit.pandorocore.enums.ChangelogEvent.UPDATE_DELETED
import com.tecknobit.pandorocore.enums.ChangelogEvent.UPDATE_PUBLISHED
import com.tecknobit.pandorocore.enums.ChangelogEvent.UPDATE_SCHEDULED
import com.tecknobit.pandorocore.enums.ChangelogEvent.UPDATE_STARTED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.group_deleted
import pandoro.composeapp.generated.resources.group_deleted_title
import pandoro.composeapp.generated.resources.invited_group
import pandoro.composeapp.generated.resources.invited_group_title
import pandoro.composeapp.generated.resources.joined_group
import pandoro.composeapp.generated.resources.joined_group_title
import pandoro.composeapp.generated.resources.left_group
import pandoro.composeapp.generated.resources.left_group_title
import pandoro.composeapp.generated.resources.project_added
import pandoro.composeapp.generated.resources.project_added_title
import pandoro.composeapp.generated.resources.project_removed
import pandoro.composeapp.generated.resources.project_removed_title
import pandoro.composeapp.generated.resources.role_changed
import pandoro.composeapp.generated.resources.role_changed_title
import pandoro.composeapp.generated.resources.update_deleted
import pandoro.composeapp.generated.resources.update_deleted_title
import pandoro.composeapp.generated.resources.update_published
import pandoro.composeapp.generated.resources.update_published_title
import pandoro.composeapp.generated.resources.update_scheduled
import pandoro.composeapp.generated.resources.update_scheduled_title
import pandoro.composeapp.generated.resources.update_started
import pandoro.composeapp.generated.resources.update_started_title

@Serializable
data class Changelog(
    val id: String,
    @SerialName(CHANGELOG_EVENT_KEY)
    val event: ChangelogEvent,
    val timestamp: Long,
    val project: Project? = null,
    val group: Group? = null,
    @SerialName(CHANGELOG_EXTRA_CONTENT_KEY)
    val extraContent: String? = null,
    @SerialName(CHANGELOG_READ_KEY)
    val read: Boolean,
) {

    @Composable
    fun getTitle() : String {
        return stringResource(
            when(event) {
                INVITED_GROUP -> Res.string.invited_group_title
                JOINED_GROUP -> Res.string.joined_group_title
                ROLE_CHANGED -> Res.string.role_changed_title
                LEFT_GROUP -> Res.string.left_group_title
                GROUP_DELETED -> Res.string.group_deleted_title
                PROJECT_ADDED -> Res.string.project_added_title
                PROJECT_REMOVED -> Res.string.project_removed_title
                UPDATE_SCHEDULED -> Res.string.update_scheduled_title
                UPDATE_STARTED -> Res.string.update_started_title
                UPDATE_PUBLISHED -> Res.string.update_published_title
                UPDATE_DELETED -> Res.string.update_deleted_title
            }
        )
    }

    /**
     * Method to get the content message<br></br>
     * No-any params required
     *
     * @return the content message in base of the [.changelogEvent] type as [String]
     */
    @Composable
    fun getContent(): String {
        val entityName = if(group != null)
            group.name
        else if(project != null)
            project.name
        else
            null
        return when(event) {
            INVITED_GROUP -> stringResource(Res.string.invited_group, entityName!!)
            JOINED_GROUP -> stringResource(Res.string.joined_group, entityName!!)
            ROLE_CHANGED -> stringResource(Res.string.role_changed, extraContent!!, entityName!!)
            LEFT_GROUP -> stringResource(Res.string.left_group, entityName!!)
            GROUP_DELETED -> stringResource(Res.string.group_deleted, entityName ?: "")
            PROJECT_ADDED -> stringResource(Res.string.project_added, entityName!!)
            PROJECT_REMOVED -> stringResource(Res.string.project_removed, entityName!!)
            UPDATE_SCHEDULED -> stringResource(Res.string.update_scheduled, entityName!!, extraContent!!)
            UPDATE_STARTED -> stringResource(Res.string.update_started, extraContent!!, entityName!!)
            UPDATE_PUBLISHED -> stringResource(Res.string.update_published, extraContent!!, entityName!!)
            UPDATE_DELETED -> stringResource(Res.string.update_deleted, extraContent!!, entityName!!)
        }
    }

    fun hasItemsData() : Boolean {
        return group != null || project != null
    }

    fun thumbnail() : String? {
        return if(group != null)
            group.logo
        else
            project?.icon
    }

    fun isInviteToGroupType() : Boolean {
        return event == INVITED_GROUP
    }

}
