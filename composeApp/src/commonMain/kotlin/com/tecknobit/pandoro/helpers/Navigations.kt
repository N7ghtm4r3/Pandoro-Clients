package com.tecknobit.pandoro.helpers

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.pandoro.ui.screens.shared.data.project.Project
import com.tecknobit.pandoro.ui.screens.shared.data.project.Update
import com.tecknobit.pandorocore.GROUP_IDENTIFIER_KEY
import com.tecknobit.pandorocore.NOTE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.PROJECT_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: NavHostController

/**
 * `SPLASHSCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.splashscreen.Splashscreen]
 */
const val SPLASHSCREEN = "Splashscreen"

/**
 * `AUTH_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.auth.presenter.AuthScreen]
 */
const val AUTH_SCREEN = "AuthScreen"

/**
 * `HOME_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen]
 */
const val HOME_SCREEN = "HomeScreen"

/**
 * `CREATE_PROJECT_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.createproject.presenter.CreateProjectScreen]
 */
const val CREATE_PROJECT_SCREEN = "CreateProject"

/**
 * `CREATE_NOTE_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.createnote.presenter.CreateNoteScreen]
 */
const val CREATE_NOTE_SCREEN = "CreateNote"

/**
 * `CREATE_CHANGE_NOTE_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.createnote.presenter.CreateNoteScreen]
 */
const val CREATE_CHANGE_NOTE_SCREEN = "CreateChangeNote"

/**
 * `SCHEDULE_UPDATE_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter.ScheduleUpdateScreen]
 */
const val SCHEDULE_UPDATE_SCREEN = "ScheduleUpdate"

/**
 * `CREATE_GROUP_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.creategroup.presenter.CreateGroupScreen]
 */
const val CREATE_GROUP_SCREEN = "CreateGroup"

/**
 * `PROJECT_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen]
 */
const val PROJECT_SCREEN = "ProjectScreen"

/**
 * `GROUP_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen]
 */
const val GROUP_SCREEN = "GroupScreen"

/**
 * Method used to navigate to the [SPLASHSCREEN] route
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToSplashscreen() {
    navigator.navigate(
        route = SPLASHSCREEN
    )
}

/**
 * Method used to navigate to the [HOME_SCREEN] route
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToHomeScreen() {
    navigator.navigate(
        route = HOME_SCREEN
    )
}

/**
 * Method used to navigate to the [CREATE_PROJECT_SCREEN] route
 *
 * @param projectId The identifier of the project to edit
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
@Wrapper
fun navToCreateProjectScreen(
    projectId: String? = null
) {
    navToCreateScreen(
        itemId = projectId,
        itemKey = PROJECT_IDENTIFIER_KEY,
        screenRoute = CREATE_PROJECT_SCREEN
    )
}

/**
 * Method used to navigate to the [CREATE_NOTE_SCREEN] route
 *
 * @param noteId The identifier of the note to edit
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
@Wrapper
fun navToCreateNoteScreen(
    noteId: String? = null
) {
    navToCreateScreen(
        itemId = noteId,
        itemKey = NOTE_IDENTIFIER_KEY,
        screenRoute = CREATE_NOTE_SCREEN
    )
}

/**
 * Method used to navigate to the [CREATE_GROUP_SCREEN] route
 *
 * @param groupId The identifier of the group to edit
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToCreateGroupScreen(
    groupId: String? = null
) {
    navToCreateScreen(
        itemId = groupId,
        itemKey = GROUP_IDENTIFIER_KEY,
        screenRoute = CREATE_GROUP_SCREEN
    )
}

/**
 * Method used to navigate to a route where the user can create or edit an item
 *
 * @param itemId The identifier of the item to edit
 * @param itemKey The key to assign to the [itemId] in the [SavedStateHandle] of the navigation
 * @param screenRoute The route to reach
 *
 * @since 1.2.0
 */
private fun navToCreateScreen(
    itemId: String?,
    itemKey: String,
    screenRoute: String
) {
    itemId?.let {
        val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.let {
            savedStateHandle[itemKey] = itemId
        }
    }
    navigator.navigate(
        route = screenRoute
    )
}

/**
 * Method used to navigate to the [CREATE_CHANGE_NOTE_SCREEN] route
 *
 * @param projectId The identifier of the protect
 * @param update The update where place the change note
 * @param noteId The identifier of the change note to edit
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToCreateChangeNoteScreen(
    projectId: String,
    update: Update,
    noteId: String? = null
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[PROJECT_IDENTIFIER_KEY] = projectId
        savedStateHandle[UPDATE_IDENTIFIER_KEY] = update.id
        savedStateHandle[UPDATE_TARGET_VERSION_KEY] = update.targetVersion
        savedStateHandle[NOTE_IDENTIFIER_KEY] = noteId
    }
    navigator.navigate(CREATE_CHANGE_NOTE_SCREEN)
}

/**
 * Method used to navigate to the [SCHEDULE_UPDATE_SCREEN] route
 *
 * @param project The protect where schedule the update
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToScheduleUpdateScreen(
    project: Project
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[PROJECT_IDENTIFIER_KEY] = project.id
        savedStateHandle[NAME_KEY] = project.name
    }
    navigator.navigate(SCHEDULE_UPDATE_SCREEN)
}

/**
 * Method used to navigate to the [PROJECT_SCREEN] route
 *
 * @param projectId The identifier of the project
 * @param updateId The identifier of the update to expand
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToProjectScreen(
    projectId: String,
    updateId: String? = null
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[PROJECT_IDENTIFIER_KEY] = projectId
        savedStateHandle[UPDATE_IDENTIFIER_KEY] = updateId
    }
    navigator.navigate(PROJECT_SCREEN)
}

/**
 * Method used to navigate to the [GROUP_SCREEN] route
 *
 * @param groupId The identifier of the group
 *
 * @since 1.2.0
 */
// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToGroupScreen(
    groupId: String
) {
    val savedStateHandle = navigator.currentBackStackEntry?.savedStateHandle
    savedStateHandle?.let {
        savedStateHandle[GROUP_IDENTIFIER_KEY] = groupId
    }
    navigator.navigate(GROUP_SCREEN)
}

@FutureEquinoxApi(
    additionalNotes = """
        Will be created a dedicated Equinox-Miscellaneous module when the Compose Navigation will be
        stable and if not provides this API will be integrated in that module
    """
)
fun SavedStateHandle.clearAll() {
    keys().forEach { key ->
        remove<Any>(key)
    }
}