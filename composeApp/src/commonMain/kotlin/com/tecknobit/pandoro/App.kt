package com.tecknobit.pandoro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.createproject.presenter.CreateProjectScreen
import com.tecknobit.pandoro.ui.screens.home.HomeScreen
import com.tecknobit.pandoro.ui.screens.splashscreen.Splashscreen
import com.tecknobit.pandoro.ui.theme.PandoroDialogTheme
import com.tecknobit.pandorocore.PROJECT_IDENTIFIER_KEY
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

const val HOME_SCREEN = "HomeScreen"

const val CREATE_PROJECT_SCREEN = "CreateProject"

const val CREATE_PROJECT_DIALOG_SCREEN = "CreateProjectDialog"

/*
/**
 * **imageLoader** -> the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
lateinit var imageLoader: ImageLoader

/**
 * **localUser** -> the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = AmetistaLocalUser()

/**
 * **requester** -> the instance to manage the requests with the backend
 */
lateinit var requester: AmetistaRequester*/

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
                route = HOME_SCREEN
            ) {
                HomeScreen().ShowContent()
            }
            scene(
                route = "$CREATE_PROJECT_SCREEN/{$PROJECT_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val projectId: String? = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)
                CreateProjectScreen(
                    projectId = projectId
                ).ShowContent()
            }
            /*dialog(
                route = "$CREATE_PROJECT_DIALOG_SCREEN/{$PROJECT_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val projectId: String? = backstackEntry.path<String>(PROJECT_IDENTIFIER_KEY)
                CreateProjectScreen(
                    projectId = projectId
                ).ShowContent()
            }*/
        }
    }
}

/**
 * Container to display an [PandoroScreen] as dialog
 *
 * @param dialogScreen: the screen to display as dialog
 */
@Composable
@NonRestartableComposable
private fun DialogScreen(
    dialogScreen: PandoroScreen<*>
) {
    PandoroDialogTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(
                        width = 425.dp,
                        height = 700.dp
                    ),
                shadowElevation = 5.dp,
                shape = RoundedCornerShape(
                    10.dp
                )
            ) {
                dialogScreen.ShowContent()
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
 * Function to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/*
/**
 * Function to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = AmetistaRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (localUser.userId == null)
        AUTH_SCREEN
    else if (localUser.password == DEFAULT_VIEWER_PASSWORD)
        CHANGE_VIEWER_PASSWORD_SCREEN
    else
        APPLICATIONS_SCREEN
    setUserLanguage()
    navigator.navigate(route)
}

/**
 * Function to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Function to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()*/

/**
 * Function to get the current screen dimension of the device where the application is running
 *
 *
 * @return the width size class based on the current dimension of the screen as [WindowWidthSizeClass]
 */
@Composable
expect fun getCurrentWidthSizeClass(): WindowWidthSizeClass

/**
 * Function to get the image picture's path
 *
 * @param imagePic: the asset from fetch its path
 *
 * @return the asset path as [String]
 */
expect fun getImagePath(
    imagePic: PlatformFile?
): String?