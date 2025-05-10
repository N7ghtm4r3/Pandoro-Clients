package com.tecknobit.pandoro.ui.screens.groups.data

import com.tecknobit.equinoxcore.helpers.CREATION_DATE_KEY
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandorocore.GROUP_DESCRIPTION_KEY
import com.tecknobit.pandorocore.GROUP_MEMBERS_KEY
import com.tecknobit.pandorocore.PROJECTS_KEY
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.Role.ADMIN
import com.tecknobit.pandorocore.enums.Role.DEVELOPER
import com.tecknobit.pandorocore.enums.Role.MAINTAINER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [Group] data class allow to represent a group data
 *
 * @property id The identifier of the group
 * @property name The name of the group
 * @property logo The logo of the group
 * @property creationDate The date when the group has been created
 * @property author The author of the group
 * @property description The description of the group
 * @property members The members list of the group
 * @property projects The projects list shared with the group
 *
 * @author N7ghtm4r3 - Tecknobit
 */
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
    val members: List<GroupMember>,
    @SerialName(PROJECTS_KEY)
    val projects: List<Project> = emptyList(),
) {

    /**
     * Method to check whether the [localUser] is the [author] of the group
     *
     * @return whether the [localUser] is the author as [Boolean]
     */
    fun iAmTheAuthor() : Boolean {
        return author.id == localUser.userId
    }

    /**
     * Method to check whether the [member] can execute some actions based his/her role
     *
     * @param member The member to check
     *
     * @return whether the [member] can execute some actions based his/her role as [Boolean]
     */
    fun checkRolePermissions(
        member: GroupMember
    ) : Boolean {
        return (member.id != localUser.userId &&
                ((iAmAMaintainer() && !member.isAnAdmin()) || iAmAnAdmin()))
    }

    /**
     * Method to check whether the [localUser] is a [MAINTAINER] of the group
     *
     * @return whether the [localUser] is a maintainer as [Boolean]
     */
    fun iAmAMaintainer() : Boolean {
        val role = findMyRole()
        return iAmAnAdmin() || role == MAINTAINER
    }

    /**
     * Method to check whether the [localUser] is a [ADMIN] of the group
     *
     * @return whether the [localUser] is an admin as [Boolean]
     */
    fun iAmAnAdmin() : Boolean {
        val role = findMyRole()
        return role == ADMIN
    }

    /**
     * Method to find the role in the group of the [localUser]
     *
     * @return the role of the [localUser] as [Role]
     */
    fun findMyRole() : Role {
        members.forEach { member ->
            if(member.id == localUser.userId)
                return member.role
        }
        return DEVELOPER
    }

}