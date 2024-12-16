package com.tecknobit.pandoro.ui.screens.projects.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUPS_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_ICON_KEY
import com.tecknobit.pandorocore.PROJECT_REPOSITORY_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import com.tecknobit.pandorocore.UPDATES_KEY
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(PROJECT_ICON_KEY)
    val icon: String? = null,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    @SerialName(AUTHOR_KEY)
    val author: PandoroUser,
    @SerialName(PROJECT_DESCRIPTION_KEY)
    val description: String,
    @SerialName(PROJECT_VERSION_KEY)
    val version: String,
    @SerialName(GROUPS_KEY)
    val groups: List<Group> = emptyList(),
    @SerialName(UPDATES_KEY)
    val updates: List<ProjectUpdate> = emptyList(),
    @SerialName(PROJECT_REPOSITORY_KEY)
    val projectRepo: String = ""
) {

    companion object {

        private const val VERSION_PREFIX = "v"

        fun String.asVersionText() : String {
            return if(this.startsWith(VERSION_PREFIX))
                return this
            else
                "$VERSION_PREFIX$this"
        }

    }

    fun getRepositoryPlatform() : RepositoryPlatform? {
        return if(projectRepo.isNotEmpty())
            RepositoryPlatform.reachPlatform(projectRepo)
        else
            null
    }

    fun amITheProjectAuthor() : Boolean {
        // return localUser.userId == author.id TODO TO USE THIS
        return true
    }

    fun isSharedWithGroups() : Boolean {
        return groups.isNotEmpty()
    }

    fun calculateTotalDevelopmentDays() : Int {
        val publishedUpdate = getPublishedUpdates()
        var totalDevelopmentDays = 0
        publishedUpdate.forEach { update ->
            totalDevelopmentDays += update.developmentDays()
        }
        return totalDevelopmentDays
    }

    fun calculateAverageDaysPerUpdate() : Double {
        val publishedUpdateNumber = getPublishedUpdates().size
        return (calculateTotalDevelopmentDays() / publishedUpdateNumber).toDouble()
    }

    private fun getPublishedUpdates(): List<ProjectUpdate> {
        return updates.filter { update -> update.status == UpdateStatus.PUBLISHED }
    }

}