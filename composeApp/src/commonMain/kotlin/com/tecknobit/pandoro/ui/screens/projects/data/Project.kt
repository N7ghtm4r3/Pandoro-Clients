package com.tecknobit.pandoro.ui.screens.projects.data

import com.tecknobit.equinoxcore.helpers.CREATION_DATE_KEY
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.GROUPS_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_ICON_KEY
import com.tecknobit.pandorocore.PROJECT_REPOSITORY_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import com.tecknobit.pandorocore.UPDATES_KEY
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [Project] data class allow to represent a project data
 *
 * @property id The identifier of the project
 * @property name The name of the project
 * @property icon The icon of the project
 * @property creationDate The date when the project has been created
 * @property author The author of the project
 * @property description The description of the project
 * @property version The current latest version available of the project
 * @property groups The groups where the project is shared
 * @property updates The list of the updates attached to the project
 * @property projectRepo The repository of the project
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class Project(
    val id: String,
    val name: String,
    @SerialName(PROJECT_ICON_KEY)
    val icon: String? = null,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    val author: PandoroUser,
    @SerialName(PROJECT_DESCRIPTION_KEY)
    val description: String,
    @SerialName(PROJECT_VERSION_KEY)
    val version: String,
    @SerialName(GROUPS_KEY)
    val groups: List<Group> = emptyList(),
    @SerialName(UPDATES_KEY)
    val updates: List<Update> = emptyList(),
    @SerialName(PROJECT_REPOSITORY_KEY)
    val projectRepo: String = ""
) {

    companion object {

        /**
         * `VERSION_PREFIX` constant value for the prefix to use for the versions values
         */
        private const val VERSION_PREFIX = "v"

        /**
         * Method to correct format a version value
         *
         * @return the version formatted as [String]
         */
        fun String.asVersionText() : String {
            return if(this.startsWith(VERSION_PREFIX))
                return this
            else
                "$VERSION_PREFIX$this"
        }

    }

    /**
     * Method to get the related platform based on the [projectRepo] value
     *
     * @return the platform as nullable [RepositoryPlatform]
     */
    fun getRepositoryPlatform() : RepositoryPlatform? {
        return if(projectRepo.isNotEmpty())
            RepositoryPlatform.reachPlatform(projectRepo)
        else
            null
    }

    /**
     * Method to check whether the [localUser] is the author of the project
     *
     * @return whether the [localUser] is the author of the project as [Boolean]
     */
    fun amITheProjectAuthor() : Boolean {
        return localUser.userId == author.id
    }

    /**
     * Method to check whether the project is shared with any groups
     *
     * @return whether the project is shared with any groups as [Boolean]
     */
    fun isSharedWithGroups() : Boolean {
        return groups.isNotEmpty()
    }

    /**
     * Method to calculate the total development days spent to publish the [updates] of the project
     *
     * @return the total development days as [Int]
     */
    fun calculateTotalDevelopmentDays() : Int {
        val publishedUpdate = getPublishedUpdates()
        var totalDevelopmentDays = 0
        publishedUpdate.forEach { update ->
            totalDevelopmentDays += update.developmentDays()
        }
        return totalDevelopmentDays
    }

    /**
     * Method to calculate the average development days spent to publish the [updates] of the project
     *
     * @return the total development days as [Double]
     */
    fun calculateAverageDaysPerUpdate() : Double {
        val publishedUpdateNumber = getPublishedUpdates().size
        if(publishedUpdateNumber == 0)
            return 0.0
        return (calculateTotalDevelopmentDays() / publishedUpdateNumber).toDouble()
    }

    /**
     * Method to extract from the [updates] list that updates that are [PUBLISHED]
     *
     * @return the filtered list of the updates as [List] of [Update]
     */
    fun getPublishedUpdates(): List<Update> {
        return updates.filter { update -> update.status == PUBLISHED }
    }

}