package com.tecknobit.pandoro.ui.screens.group.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.pandoro.commondata.PandoroUser
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_MEMBER_KEY
import com.tecknobit.pandorocore.PROJECTS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    @SerialName(AUTHOR_KEY)
    val author: PandoroUser,
    @SerialName(GROUP_DESCRIPTION_KEY)
    val description: String,
    @SerialName(GROUP_MEMBER_KEY)
    val members: List<PandoroUser>,
    @SerialName(PROJECTS_KEY)
    val projects: List<Project>
)