package com.tecknobit.pandoro.ui.screens.groups.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandoro.ui.commondata.PandoroUser
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_LOGO_KEY
import com.tecknobit.pandorocore.GROUP_MEMBER_KEY
import com.tecknobit.pandorocore.PROJECTS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Group(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(GROUP_LOGO_KEY)
    val logo: String,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    @SerialName(AUTHOR_KEY)
    val author: PandoroUser,
    @SerialName(GROUP_DESCRIPTION_KEY)
    val description: String,
    @SerialName(GROUP_MEMBER_KEY)
    val members: List<PandoroUser>, // TODO: TO CHANGE IN HashSet
    @SerialName(PROJECTS_KEY)
    val projects: List<Project>,
) {

    fun iAmTheAuthor() : Boolean {
        // TODO: USE THE LOCAL USER ID
        return true
    }

    fun iAmAMaintainer() : Boolean {
        // TODO: USE THE LOCAL USER ID
        return iAmAnAdmin() || true
    }

    fun iAmAnAdmin() : Boolean {
        // TODO: USE THE LOCAL USER ID
        return true
    }

}