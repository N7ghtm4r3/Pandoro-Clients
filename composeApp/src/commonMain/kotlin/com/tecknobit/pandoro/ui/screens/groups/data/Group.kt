package com.tecknobit.pandoro.ui.screens.groups.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.AUTHOR_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_LOGO_KEY
import com.tecknobit.pandorocore.GROUP_MEMBER_KEY
import com.tecknobit.pandorocore.PROJECTS_KEY
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.Role.ADMIN
import com.tecknobit.pandorocore.enums.Role.MAINTAINER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

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
    val members: List<GroupMember>, // TODO: TO CHANGE IN HashSet
    @SerialName(PROJECTS_KEY)
    val projects: List<Project>,
) {

    fun iAmTheAuthor() : Boolean {
        // TODO: USE THE LOCAL USER ID
        // author.id == localUser.id
        return Random.nextBoolean()
    }

    fun iAmAMaintainer() : Boolean {
        val role = findMyRole()
        return iAmAnAdmin() || role == MAINTAINER
    }

    fun iAmAnAdmin() : Boolean {
        val role = findMyRole()
        return role == ADMIN
    }

    fun findMyRole() : Role {
        // TODO: USE THE LOCAL USER ID
        /*members.forEach { member ->
            if(member.id == localUser.id)
                return member.role
        }*/
        return Role.MAINTAINER//Role.entries[Random.nextInt(3)]
    }

}