package com.tecknobit.pandoro.ui.screens.projects.data

import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandorocore.CREATION_DATE_KEY
import com.tecknobit.pandorocore.PROJECT_DESCRIPTION_KEY
import com.tecknobit.pandorocore.PROJECT_VERSION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(CREATION_DATE_KEY)
    val creationDate: Long,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(PROJECT_DESCRIPTION_KEY)
    val description: String,
    @SerialName(PROJECT_VERSION_KEY)
    val version: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(NAME_KEY)
    val name: String,
    @SerialName(NAME_KEY)
    val name: String,
)