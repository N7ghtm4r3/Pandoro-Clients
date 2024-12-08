package com.tecknobit.pandoro.ui.screens.shared.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.EMAIL_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.PROFILE_PIC_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.SURNAME_KEY
import com.tecknobit.pandorocore.MEMBER_ROLE_KEY
import com.tecknobit.pandorocore.enums.Role
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
    val role: Role
)
