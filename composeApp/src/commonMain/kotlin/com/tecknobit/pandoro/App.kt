@file:OptIn(ExperimentalResourceApi::class)

package com.tecknobit.pandoro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.AmetistaEngine.Companion.FILES_AMETISTA_CONFIG_PATHNAME
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcompose.session.screens.equinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.pandoro.helpers.AUTH_SCREEN
import com.tecknobit.pandoro.helpers.CREATE_CHANGE_NOTE_SCREEN
import com.tecknobit.pandoro.helpers.CREATE_GROUP_SCREEN
import com.tecknobit.pandoro.helpers.CREATE_NOTE_SCREEN
import com.tecknobit.pandoro.helpers.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.helpers.GROUP_SCREEN
import com.tecknobit.pandoro.helpers.HOME_SCREEN
import com.tecknobit.pandoro.helpers.PROJECT_SCREEN
import com.tecknobit.pandoro.helpers.PandoroRequester
import com.tecknobit.pandoro.helpers.SCHEDULE_UPDATE_SCREEN
import com.tecknobit.pandoro.helpers.SPLASHSCREEN
import com.tecknobit.pandoro.helpers.clearAll
import com.tecknobit.pandoro.helpers.customHttpClient
import com.tecknobit.pandoro.helpers.navToSplashscreen
import com.tecknobit.pandoro.helpers.navigator
import com.tecknobit.pandoro.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.pandoro.ui.screens.creategroup.presenter.CreateGroupScreen
import com.tecknobit.pandoro.ui.screens.createnote.presenter.CreateNoteScreen
import com.tecknobit.pandoro.ui.screens.createproject.presenter.CreateProjectScreen
import com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter.ScheduleUpdateScreen
import com.tecknobit.pandoro.ui.screens.splashscreen.Splashscreen
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandorocore.GROUP_IDENTIFIER_KEY
import com.tecknobit.pandorocore.NOTE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.PROJECT_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.oswald
import pandoro.composeapp.generated.resources.robotomono

/**
 * `bodyFontFamily` the Pandoro's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * `displayFontFamily` the Pandoro's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * `imageLoader` the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

/**
 * `requester` the instance to manage the requests with the backend
 */
lateinit var requester: PandoroRequester

/**
 * `localUser` the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = EquinoxLocalUser("Pandoro")

/**
 * Common entry point of the **Pandoro** application
 */
@OptIn(ExperimentalComposeApi::class, ExperimentalStdlibApi::class)
@Composable
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.robotomono))
    displayFontFamily = FontFamily(Font(Res.font.oswald))
    // InitAmetista()
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = customHttpClient()
                )
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()

    // TODO: TO USE THIS UNIQUE THEME CALL 
    // PandoroTheme {
        navigator = rememberNavController()
        NavHost(
            navController = navigator,
            startDestination = SPLASHSCREEN
        ) {
            composable(
                route = SPLASHSCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme { 
                    val splashscreen = equinoxScreen { Splashscreen() }
                    splashscreen.ShowContent()
                }
            }
            composable(
                route = AUTH_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    val authScreen = equinoxScreen { AuthScreen() }
                    authScreen.ShowContent()
                }
            }
            composable(
                route = HOME_SCREEN
            ) { 
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    navigator.currentBackStackEntry?.savedStateHandle?.clearAll()
                    val homeScreen = equinoxScreen { HomeScreen() }
                    homeScreen.ShowContent()
                }
            }
            composable(
                route = CREATE_PROJECT_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val projectId: String? = savedStateHandle[PROJECT_IDENTIFIER_KEY]
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    val createProjectScreen = equinoxScreen {
                        CreateProjectScreen(
                            projectId = projectId
                        )
                    }
                    createProjectScreen.ShowContent()
                    savedStateHandle.remove<String>(PROJECT_IDENTIFIER_KEY)
                }
            }
            composable(
                route = CREATE_NOTE_SCREEN
            ) {
                // TODO: TO REMOVE THIS THEME CALL
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val noteId: String? = savedStateHandle[NOTE_IDENTIFIER_KEY]
                PandoroTheme {
                    val createNoteScreen = equinoxScreen {
                        CreateNoteScreen(
                            noteId = noteId
                        )
                    }
                    createNoteScreen.ShowContent()
                    savedStateHandle.remove<String>(NOTE_IDENTIFIER_KEY)
                }
            }
            composable(
                route = CREATE_CHANGE_NOTE_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val projectId: String = savedStateHandle[PROJECT_IDENTIFIER_KEY]!!
                val updateId: String? = savedStateHandle[UPDATE_IDENTIFIER_KEY]
                val targetVersion: String? = savedStateHandle[UPDATE_TARGET_VERSION_KEY]
                val noteId: String? = savedStateHandle[NOTE_IDENTIFIER_KEY]
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    updateId?.let {
                        val createChangeNoteScreen = equinoxScreen {
                            CreateNoteScreen(
                                projectId = projectId,
                                updateId = updateId,
                                targetVersion = targetVersion,
                                noteId = noteId
                            )
                        }
                        createChangeNoteScreen.ShowContent()
                    }
                }
            }
            composable(
                route = SCHEDULE_UPDATE_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val projectId: String = savedStateHandle[PROJECT_IDENTIFIER_KEY]!!
                val projectName: String? = savedStateHandle[NAME_KEY]
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    projectName?.let {
                        val scheduleUpdateScreen = equinoxScreen {
                            ScheduleUpdateScreen(
                                projectId = projectId,
                                projectName = projectName
                            )
                        }
                        scheduleUpdateScreen.ShowContent()
                    }
                }
            }
            composable(
                route = CREATE_GROUP_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val groupId: String? = savedStateHandle[GROUP_IDENTIFIER_KEY]
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    val createGroupScreen = equinoxScreen {
                        CreateGroupScreen(
                            groupId = groupId
                        )
                    }
                    createGroupScreen.ShowContent()
                }
            }
            composable(
                route = PROJECT_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val projectId: String? = savedStateHandle[PROJECT_IDENTIFIER_KEY]
                val updateId: String? = savedStateHandle[UPDATE_IDENTIFIER_KEY]
                projectId?.let {
                    // TODO: TO REMOVE THIS THEME CALL
                    PandoroTheme {
                        val projectScreen = equinoxScreen {
                            ProjectScreen(
                                projectId = projectId,
                                updateToExpandId = updateId
                            )
                        }
                        projectScreen.ShowContent()
                    }
                }
            }
            composable(
                route = GROUP_SCREEN
            ) {
                val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                val groupId: String? = savedStateHandle[GROUP_IDENTIFIER_KEY]
                // TODO: TO REMOVE THIS THEME CALL
                PandoroTheme {
                    groupId?.let {
                        val groupScreen = equinoxScreen {
                            GroupScreen(
                                groupId = groupId
                            )
                        }
                        groupScreen.ShowContent()
                    }
                }
            }
        }    
    // }
    SessionFlowState.invokeOnUserDisconnected {
        localUser.clear()
        navToSplashscreen()
    }
}

/**
 * Method used to initialize the Ametista system
 */
@Composable
// TODO: TO REIMPLEMENT WHEN NECESSARY
private fun InitAmetista() {
    LaunchedEffect(Unit) {
        val ametistaEngine = AmetistaEngine.ametistaEngine
        ametistaEngine.fireUp(
            configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME),
            host = AmetistaConfig.HOST,
            serverSecret = AmetistaConfig.SERVER_SECRET!!,
            applicationId = AmetistaConfig.APPLICATION_IDENTIFIER!!,
            bypassSslValidation = AmetistaConfig.BYPASS_SSL_VALIDATION,
            debugMode = false
        )
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = PandoroRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken,
        debugMode = true // TODO: TO REMOVE
    )
    val route = if (localUser.isAuthenticated) {
        MainScope().launch {
            requester.sendRequest(
                request = {
                    getDynamicAccountData()
                },
                onSuccess = { response ->
                    localUser.updateDynamicAccountData(
                        dynamicData = response.toResponseData()
                    )
                    setUserLanguage()
                },
                onFailure = { setUserLanguage() }
            )
        }
        HOME_SCREEN
    } else
        AUTH_SCREEN
    navigator.navigate(route)
}

/**
 * Method to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
expect fun CloseApplicationOnNavBack()