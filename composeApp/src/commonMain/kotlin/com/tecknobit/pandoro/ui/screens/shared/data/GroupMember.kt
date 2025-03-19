package com.tecknobit.pandoro.ui.screens.shared.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import com.tecknobit.equinoxcore.helpers.EMAIL_KEY
import com.tecknobit.equinoxcore.helpers.IDENTIFIER_KEY
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import com.tecknobit.equinoxcore.helpers.SURNAME_KEY
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandoro.ui.theme.Yellow
import com.tecknobit.pandorocore.INVITATION_STATUS_KEY
import com.tecknobit.pandorocore.MEMBER_ROLE_KEY
import com.tecknobit.pandorocore.enums.InvitationStatus
import com.tecknobit.pandorocore.enums.InvitationStatus.JOINED
import com.tecknobit.pandorocore.enums.InvitationStatus.PENDING
import com.tecknobit.pandorocore.enums.Role
import com.tecknobit.pandorocore.enums.Role.ADMIN
import com.tecknobit.pandorocore.enums.Role.DEVELOPER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [GroupMember] data class allow to represent a member of a group data
 *
 * @property id The identifier of the member
 * @property profilePic The profile picture of the member
 * @property name The name of the member
 * @property surname The surname of the member
 * @property email The email of the member
 * @property role The role of the member
 * @property status The current status of the member
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class GroupMember(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(PROFILE_PIC_KEY)
    val profilePic: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(SURNAME_KEY)
    val surname: String,
    @SerialName(EMAIL_KEY)
    val email: String,
    @SerialName(MEMBER_ROLE_KEY)
    val role: Role = DEVELOPER,
    @SerialName(INVITATION_STATUS_KEY)
    val status: InvitationStatus = PENDING
) {

    companion object {

        /**
         * Method to format as text a [Role] value
         *
         * @return role formated as [String]
         */
        fun Role.asText() : String {
            return this.name.lowercase().capitalize()
        }

        /**
         * Method to get the related [Role] color value
         *
         * @return role related color as [Color]
         */
        @Composable
        fun Role.color() : Color {
            return if(this == ADMIN)
                MaterialTheme.colorScheme.error
            else
                Unspecified
        }

        /**
         * Method to format as text a [InvitationStatus] value
         *
         * @return status formated as [String]
         */
        fun InvitationStatus.asText() : String {
            return this.name.lowercase().capitalize()
        }

        /**
         * Method to get the related [InvitationStatus] color value
         *
         * @return status related color as [Color]
         */
        @Composable
        fun InvitationStatus.color() : Color {
            return if(this == PENDING)
                Yellow()
            else
                Green()
        }

    }

    /**
     * Method to obtain the complete name of the member
     *
     * @return the complete name ([name] and [surname]) as [String]
     */
    fun completeName() : String {
        return "$name $surname"
    }

    /**
     * Method to check whether the current [status] of the member is [JOINED]
     *
     * @return whether the member [JOINED] as [Boolean]
     */
    fun joined() : Boolean {
        return status == JOINED
    }

    /**
     * Method to check whether the current [role] of the member is [ADMIN]
     *
     * @return whether the member [ADMIN] as [Boolean]
     */
    fun isAnAdmin() : Boolean {
        return role == ADMIN
    }

}
