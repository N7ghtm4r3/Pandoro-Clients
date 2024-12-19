package com.tecknobit.pandoro.ui.screens.shared.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.EMAIL_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.PROFILE_PIC_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.SURNAME_KEY
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
import com.tecknobit.pandorocore.enums.Role.MAINTAINER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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

        fun Role.asText() : String {
            return this.name.lowercase().capitalize()
        }

        @Composable
        fun Role.color() : Color {
            return if(this == ADMIN)
                MaterialTheme.colorScheme.error
            else
                Unspecified
        }

        fun InvitationStatus.asText() : String {
            return this.name.lowercase().capitalize()
        }

        @Composable
        fun InvitationStatus.color() : Color {
            return if(this == PENDING)
                Yellow()
            else
                Green()
        }

    }

    fun completeName() : String {
        return "$name $surname"
    }

    fun joined() : Boolean {
        return status == JOINED
    }

    fun isAMaintainer() : Boolean {
        return isAnAdmin() || role == MAINTAINER
    }

    fun isAnAdmin() : Boolean {
        return role == ADMIN
    }

}
