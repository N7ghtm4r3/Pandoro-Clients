package com.tecknobit.pandoro.helpers

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavHostController
import com.tecknobit.equinoxcore.annotations.FutureEquinoxApi
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Update
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


// TODO: TO DOCUMENT 1.2.0

// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToSplashscreen() {
    navigator.navigate(
        route = SPLASHSCREEN
    )
}

// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
fun navToHomeScreen() {
    navigator.navigate(
        route = HOME_SCREEN
    )
}

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

// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
@Wrapper
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

// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
@Wrapper
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

// TODO: TO ANNOTATE WITH THE DestinationScreen ANNOTATION
@Wrapper
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