package com.tecknobit.pandoro.ui.screens.projects.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandoro.commondata.PandoroUser
import com.tecknobit.pandoro.ui.screens.group.data.Group
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUPS_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_REPOSITORY_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import com.tecknobit.pandorocore.UPDATES_KEY
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(NAME_KEY)
    val name: String,
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

    fun getRepositoryPlatform() : RepositoryPlatform? {
        return if(projectRepo.isNotEmpty())
            RepositoryPlatform.reachPlatform(projectRepo)
        else
            null
    }

}