package com.tecknobit.pandoro.ui.screens.groups.data

import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_MEMBERS_KEY
import com.tecknobit.pandorocore.PROJECTS_KEY
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.Role.ADMIN
import com.tecknobit.pandorocore.enums.Role.MAINTAINER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class Group(
    val id: String,
    val name: String,
    val logo: String,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    val author: PandoroUser,
    @SerialName(GROUP_DESCRIPTION_KEY)
    val description: String,
    @SerialName(GROUP_MEMBERS_KEY)
    val members: List<GroupMember>, // TODO: TO CHANGE IN HashSet
    @SerialName(PROJECTS_KEY)
    val projects: List<Project> = emptyList(),
) {

    fun iAmTheAuthor() : Boolean {
        // TODO: USE THE LOCAL USER ID
        // author.id == localUser.id
        return Random.nextBoolean()
    }

    fun checkRolePermissions(
        member: GroupMember
    ) : Boolean {
        return (member.id != localUser.userId &&
                ((iAmAMaintainer() && !member.isAnAdmin()) || iAmAnAdmin()))
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
        return Role.ADMIN//Role.entries[Random.nextInt(3)]
    }

}