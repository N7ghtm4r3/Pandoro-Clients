package com.tecknobit.pandoro.ui.screens.shared.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.EMAIL_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.IDENTIFIER_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.PROFILE_PIC_KEY
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.SURNAME_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The [PandoroUser] data class allow to represent a user data
 *
 * @property id The identifier of the user
 * @property profilePic The profile picture of the user
 * @property name The name of the user
 * @property surname The surname of the user
 * @property email The email of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class PandoroUser(
    @SerialName(IDENTIFIER_KEY)
    val id: String,
    @SerialName(PROFILE_PIC_KEY)
    val profilePic: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(SURNAME_KEY)
    val surname: String,
    @SerialName(EMAIL_KEY)
    val email: String
) {

    /**
     * Method to obtain the complete name of the user
     *
     * @return the complete name ([name] and [surname]) as [String]
     */
    fun completeName() : String {
        return "$name $surname"
    }

}