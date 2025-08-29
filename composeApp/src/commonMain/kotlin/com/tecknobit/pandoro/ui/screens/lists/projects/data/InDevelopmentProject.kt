package com.tecknobit.pandoro.ui.screens.lists.projects.data

/**
 * The [InDevelopmentProject] data class allow to represent a project currently in
 * [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT]
 *
 * @property project The project owner of the [update]
 * @property update The update in [com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT]
 *
 * @author N7ghtm4r3 - Tecknobit
 */
data class InDevelopmentProject(
    val project: Project,
    val update: Update
)