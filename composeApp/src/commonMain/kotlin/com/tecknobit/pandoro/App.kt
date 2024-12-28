package com.tecknobit.pandoro

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.NAME_KEY
import com.tecknobit.pandoro.helpers.PandoroLocalUser
import com.tecknobit.pandoro.helpers.PandoroRequester
import com.tecknobit.pandoro.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.pandoro.ui.screens.creategroup.presenter.CreateGroupScreen
import com.tecknobit.pandoro.ui.screens.createnote.presenter.CreateNoteScreen
import com.tecknobit.pandoro.ui.screens.createproject.presenter.CreateProjectScreen
import com.tecknobit.pandoro.ui.screens.group.presenter.GroupScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen.Companion.GROUPS_SCREEN
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen.Companion.NOTES_SCREEN
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen.Companion.PROJECTS_SCREEN
import com.tecknobit.pandoro.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter.ScheduleUpdateScreen
import com.tecknobit.pandoro.ui.screens.splashscreen.Splashscreen
import com.tecknobit.pandorocore.GROUP_IDENTIFIER_KEY
import com.tecknobit.pandorocore.NOTE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.PROJECT_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_IDENTIFIER_KEY
import com.tecknobit.pandorocore.UPDATE_TARGET_VERSION_KEY
import io.github.vinceglb.filekit.core.PlatformFile
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.oswald
import pandoro.composeapp.generated.resources.robotomono
import javax.net.ssl.SSLContext

/**
 * **bodyFontFamily** -> the Pandoro's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * **displayFontFamily** -> the Pandoro's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * **navigator** -> the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

/**
 * **sslContext** -> the context helper to TLS protocols
 */
private val sslContext = SSLContext.getInstance("TLS")

const val SPLASHSCREEN = "Splashscreen"

const val AUTH_SCREEN = "AuthScreen"

const val HOME_SCREEN = "HomeScreen"

const val CREATE_PROJECT_SCREEN = "CreateProject"

const val CREATE_NOTE_SCREEN = "CreateNote"

const val CREATE_CHANGE_NOTE_SCREEN = "CreateChangeNote"

const val SCHEDULE_UPDATE_SCREEN = "ScheduleUpdate"

const val CREATE_GROUP_SCREEN = "CreateGroup"

const val PROJECT_SCREEN = "ProjectScreen"

const val GROUP_SCREEN = "GroupScreen"

/*
/**
 * **imageLoader** -> the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader*/

/**
 * **requester** -> the instance to manage the requests with the backend
 */
lateinit var requester: PandoroRequester

/**
 * **localUser** -> the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = PandoroLocalUser()

/**
 * Common entry point of the **Ametista** application
 *
 */
@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.robotomono))
    displayFontFamily = FontFamily(Font(Res.font.oswald))
    /*sslContext.init(null, validateSelfSignedCertificate(), SecureRandom())
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                OkHttpNetworkFetcherFactory {
                    OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.socketFactory,
                            validateSelfSignedCertificate()[0] as X509TrustManager
                        )
                        .hostnameVerifier { _: String?, _: SSLSession? -> true }
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .build()
                }
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()*/
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASHSCREEN
        ) {
            scene(
                route = SPLASHSCREEN
            ) {
                Splashscreen().ShowContent()
            }
            scene(
                route = AUTH_SCREEN
            ) {
                AuthScreen().ShowContent()
            }
            scene(
                route = HOME_SCREEN
            ) {
                HomeScreen().ShowContent()
            }
            scene(
                route = "$CREATE_PROJECT_SCREEN/{$PROJECT_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val projectId: String? = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)
                HomeScreen.setCurrentScreenDisplayed(
                    screen = PROJECTS_SCREEN
                )
                CreateProjectScreen(
                    projectId = projectId
                ).ShowContent()
            }
            scene(
                route = "$CREATE_NOTE_SCREEN/{$NOTE_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val noteId: String? = backstackEntry.path<String>(NOTE_IDENTIFIER_KEY)
                HomeScreen.setCurrentScreenDisplayed(
                    screen = NOTES_SCREEN
                )
                CreateNoteScreen(
                    noteId = noteId
                ).ShowContent()
            }
            scene(
                route = "$CREATE_CHANGE_NOTE_SCREEN/{${PROJECT_IDENTIFIER_KEY}}/{${UPDATE_IDENTIFIER_KEY}}" +
                        "/{${UPDATE_TARGET_VERSION_KEY}}/{$NOTE_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val projectId: String = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)!!
                val updateId: String = backstackEntry.path<String>(UPDATE_IDENTIFIER_KEY)!!
                val targetVersion: String = backstackEntry.path<String>(UPDATE_TARGET_VERSION_KEY)!!
                val noteId: String? = backstackEntry.path<String>(NOTE_IDENTIFIER_KEY)
                CreateNoteScreen(
                    projectId = projectId,
                    updateId = updateId,
                    targetVersion = targetVersion,
                    noteId = noteId
                ).ShowContent()
            }
            scene(
                route = "$SCHEDULE_UPDATE_SCREEN/{$PROJECT_IDENTIFIER_KEY}/{$NAME_KEY}"
            ) { backstackEntry ->
                val projectId: String = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)!!
                val projectName: String = backstackEntry.path<String>(NAME_KEY)!!
                ScheduleUpdateScreen(
                    projectId = projectId,
                    projectName = projectName
                ).ShowContent()
            }
            scene(
                route = "$CREATE_GROUP_SCREEN/{$GROUP_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val groupId: String? = backstackEntry.path<String>(GROUP_IDENTIFIER_KEY)
                HomeScreen.setCurrentScreenDisplayed(
                    screen = GROUPS_SCREEN
                )
                CreateGroupScreen(
                    groupId = groupId
                ).ShowContent()
            }
            scene(
                route = "$PROJECT_SCREEN/{$PROJECT_IDENTIFIER_KEY}/{$UPDATE_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val projectId: String = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)!!
                val updateId: String? = backstackEntry.path<String>(UPDATE_IDENTIFIER_KEY)
                ProjectScreen(
                    projectId = projectId,
                    updateToExpandId = updateId
                ).ShowContent()
            }
            scene(
                route = "$GROUP_SCREEN/{$GROUP_IDENTIFIER_KEY}"
            ) { backstackEntry ->
                val groupId: String = backstackEntry.path<String>(GROUP_IDENTIFIER_KEY)!!
                GroupScreen(
                    groupId = groupId
                ).ShowContent()
            }
        }
    }
}

/*
/**
 * Method to validate a self-signed SLL certificate and bypass the checks of its validity<br></br>
 *
 * @return list of trust managers as [Array] of [TrustManager]
 * @apiNote this method disable all checks on the SLL certificate validity, so is recommended to
 * use for test only or in a private distribution on own infrastructure
 */
private fun validateSelfSignedCertificate(): Array<TrustManager> {
    return arrayOf(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return arrayOf()
        }

        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    })
}*/

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = PandoroRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (localUser.userId == null)
        AUTH_SCREEN
    else
        HOME_SCREEN
    setUserLanguage()
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
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()

/**
 * Method to get the current screen dimension of the device where the application is running
 *
 *
 * @return the width size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
fun getCurrentWidthSizeClass(): WindowWidthSizeClass {
    return getCurrentSizeClass().widthSizeClass
}

/**
 * Method to get the current screen dimension of the device where the application is running
 *
 *
 * @return the size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
expect fun getCurrentSizeClass(): WindowSizeClass

/**
 * Method to get the image picture's path
 *
 * @param imagePic: the asset from fetch its path
 *
 * @return the asset path as [String]
 */
expect fun getImagePath(
    imagePic: PlatformFile?
): String?

/**
 * Method to copy to the clipboard a content value
 *
 * @param content The content to copy
 * @param onCopy The action to execute after the copy in the clipboard
 */
expect fun copyToClipboard(
    content: String,
    onCopy: () -> Unit = {}
)