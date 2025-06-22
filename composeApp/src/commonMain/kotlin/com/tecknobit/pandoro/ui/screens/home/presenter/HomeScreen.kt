package com.tecknobit.pandoro.ui.screens.home.presenter

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxnavigation.I18nNavigationTab
import com.tecknobit.equinoxnavigation.NavigatorScreen
import com.tecknobit.pandoro.CloseApplicationOnNavBack
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.icons.Activity
import com.tecknobit.pandoro.ui.screens.groups.presenter.GroupsScreen
import com.tecknobit.pandoro.ui.screens.home.presentation.HomeScreenViewModel
import com.tecknobit.pandoro.ui.screens.notes.presenter.NotesScreen
import com.tecknobit.pandoro.ui.screens.overview.presenter.OverviewScreen
import com.tecknobit.pandoro.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.pandoro.ui.screens.projects.presenter.ProjectsScreen
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.app_version
import pandoro.composeapp.generated.resources.groups
import pandoro.composeapp.generated.resources.notes
import pandoro.composeapp.generated.resources.overview
import pandoro.composeapp.generated.resources.profile
import pandoro.composeapp.generated.resources.projects

/**
 * The [HomeScreen] class is used as container for the main screens of the application and manage the
 * navigation in app
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
 *
 */
@OptIn(ExperimentalComposeApi::class)
class HomeScreen: NavigatorScreen<I18nNavigationTab>() {

    companion object {

        /**
         * `MAX_CHANGELOGS_DISPLAYABLE_VALUE` the max value displayable in the unread changelogs badge
         */
        private const val MAX_CHANGELOGS_DISPLAYABLE_VALUE = 99

        /**
         * `PROJECTS_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.projects.presenter.ProjectsScreen]
         */
        const val PROJECTS_SCREEN = "ProjectsScreen"

        /**
         * `NOTES_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.notes.presenter.NotesScreen]
         */
        const val NOTES_SCREEN = "NotesScreen"

        /**
         * `OVERVIEW_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.overview.presenter.OverviewScreen]
         */
        const val OVERVIEW_SCREEN = "OverviewScreen"

        /**
         * `GROUPS_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.groups.presenter.GroupsScreen]
         */
        const val GROUPS_SCREEN = "GroupsScreen"

        /**
         * `PROFILE_SCREEN` route to navigate to the [com.tecknobit.pandoro.ui.screens.profile.presenter.ProfileScreen]
         */
        const val PROFILE_SCREEN = "ProfileScreen"

        /**
         * `destinations` the list of the destination reachable by the navigation
         */
        private val destinations = listOf<NavigationTab>(

        )

        /**
         * `currentScreenDisplayed` the index number of the current [destinations] displayed
         */
        private var currentScreenDisplayed: Int = 0

        /**
         * `isBottomNavigationMode` whether the navigation mode is the side or bottom one
         */
        //lateinit var isBottomNavigationMode: MutableState<Boolean>

        /**
         * Method to set the current screen displayed before navigating in other one
         *
         * @param screen The current screen displayed
         */
        @Deprecated(level = DeprecationLevel.ERROR, message = "")
        fun setCurrentScreenDisplayed(
            screen: String
        ) {
            currentScreenDisplayed = when(screen) {
                NOTES_SCREEN -> 1
                OVERVIEW_SCREEN -> 2
                GROUPS_SCREEN -> 3
                PROFILE_SCREEN -> 4
                else -> 0
            }
        }

    }

    private val viewModel = HomeScreenViewModel()

    /**
     * `unreadChangelogs` the state of the unread changelogs number
     */
    private lateinit var unreadChangelogs: State<Int>

    /**
     * `currentDestination` the current destination displayed
     */
    private lateinit var currentDestination: MutableState<NavigationTab>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        PandoroTheme {
            NavigationContent(
                backgroundTab = MaterialTheme.colorScheme.primary
            )
        }

        /*Box (
            modifier = Modifier
                .fillMaxSize()
        ) {
            PandoroTheme {
                when(currentDestination.value.tabIdentifier) {
                    PROJECTS_SCREEN -> { ProjectsScreen().ShowContent() }
                    NOTES_SCREEN -> { NotesScreen().ShowContent() }
                    OVERVIEW_SCREEN -> { OverviewScreen().ShowContent() }
                    GROUPS_SCREEN -> { GroupsScreen().ShowContent() }
                    PROFILE_SCREEN -> { ProfileScreen().ShowContent() }
                }
            }
            ResponsiveContent(
                onExpandedSizeClass = { SideNavigationBar() },
                onMediumSizeClass = { SideNavigationBar() },
                onCompactSizeClass = {
                    BottomNavigationBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
            )
        }*/
    }

    /**
     * The sidebar used to manage the navigation
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun SideNavigationBar() {
        NavigationRail(
            modifier = Modifier
                .width(125.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            header = {
                ProfilePic(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        ),
                    size = 75.dp,
                    onClick = { currentDestination.value = destinations.last() }
                )
            }
        ) {
            destinations.forEach { destination ->
                if(destination.tabIdentifier != PROFILE_SCREEN) {
                    NavigationRailItem(
                        selected = destination == currentDestination.value,
                        icon = {
                            Icon(
                                imageVector = destination.icon!!,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(destination.title),
                                fontFamily = displayFontFamily
                            )
                        },
                        alwaysShowLabel = false,
                        onClick = { currentDestination.value = destination }
                    )
                }
            }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        bottom = 16.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "v" + stringResource(Res.string.app_version),
                    fontFamily = displayFontFamily,
                    fontSize = 14.sp
                )
            }
        }
    }

    /**
     * The bottom bar used to manage the navigation
     *
     * @param modifier The modifier to apply to the navigation bar
     */
    @Composable
    private fun BottomNavigationBar(
        modifier: Modifier = Modifier
    ) {
        BottomAppBar(
            modifier = modifier
        ) {
            destinations.forEach { destination ->
                NavigationBarItem(
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    selected = destination == currentDestination.value,
                    icon = { destination.TabIcon() },
                    label = {
                        Text(
                            text = stringResource(destination.title),
                            fontFamily = displayFontFamily
                        )
                    },
                    alwaysShowLabel = false,
                    onClick = { currentDestination.value = destination }
                )
            }
        }
    }

    /**
     * The icon to use in each tab of the navigation bars
     */
    @Composable
    @NonRestartableComposable
    private fun NavigationTab.TabIcon() {
        val icon = this.icon
        if(icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        } else
            ProfilePic()
    }

    /**
     * The profile picture of the [com.tecknobit.pandoro.localUser] used as tab also
     *
     * @param modifier The modifier to apply to component
     * @param size The size of the profile picture
     * @param onClick The action to execute when the profile picture is clicked
     */
    @Composable
    private fun ProfilePic(
        modifier: Modifier = Modifier,
        size: Dp = 35.dp,
        onClick: (() -> Unit)? = null
    ) {
        BadgedBox(
            modifier = modifier,
            badge = {
                androidx.compose.animation.AnimatedVisibility(
                    visible = unreadChangelogs.value > 0,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    PandoroTheme {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.BottomEnd),
                            containerColor = MaterialTheme.colorScheme.error
                        ) {
                            Text(
                                text = unreadChangelogs.value.formatUnreadChangelogsValue()
                            )
                        }
                    }
                }
            }
        ) {
            Thumbnail(
                size = size,
                thumbnailData = localUser.profilePic,
                contentDescription = "Profile pic",
                onClick = onClick
            )
        }
    }

    /**
     * Method to format as presentable text the [unreadChangelogs] number
     *
     * @return the formatted text as [String]
     */
    private fun Int.formatUnreadChangelogsValue() : String {
        return if(this > MAX_CHANGELOGS_DISPLAYABLE_VALUE)
            return "$MAX_CHANGELOGS_DISPLAYABLE_VALUE+"
        else
            this.toString()
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.countUnreadChangelogs()
    }

    /**
     * Method used to instantiate the related screen based on the current [activeNavigationTabIndex]
     *
     * @return the screen as [EquinoxNoModelScreen]
     */
    override fun Int.tabContent(): EquinoxNoModelScreen {
        return when(this) {
            0 -> { ProjectsScreen() }
            1 -> { NotesScreen() }
            2 -> { OverviewScreen() }
            3 -> { GroupsScreen() }
            else -> { ProfileScreen() }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        super.CollectStates()
       // currentDestination = remember { mutableStateOf(destinations[currentScreenDisplayed]) }
        // isBottomNavigationMode = remember { mutableStateOf(false) }
        unreadChangelogs = viewModel.unreadChangelog.collectAsState()
    }

    /**
     * Method used to retrieve the tabs to assign to the [tabs] array
     *
     * @return the tabs used by the [NavigatorScreen] as [Array] of [I18nNavigationTab]
     */
    override fun navigationTabs(): Array<I18nNavigationTab> {
        return arrayOf(
            I18nNavigationTab(
                icon = Icons.Default.Folder,
                title = Res.string.projects,
                contentDescription = "Projects"
            ),
            I18nNavigationTab(
                icon = Icons.AutoMirrored.Filled.Notes,
                title = Res.string.notes,
                contentDescription = "notes"
            ),
            I18nNavigationTab(
                icon = Activity,
                title = Res.string.overview,
                contentDescription = "overview"
            ),
            I18nNavigationTab(
                icon = Icons.Default.Groups3,
                title = Res.string.groups,
                contentDescription = "groups"
            ),
            I18nNavigationTab(
                icon = Icons.Default.AccountBox,
                title = Res.string.profile,
                contentDescription = "profile"
            )
        )
    }

    /**
     * The [NavigationTab] data class allow to represent a navigation tab
     *
     * @property tabIdentifier The identifier of the tab
     * @property icon The representative icon of the tab
     * @property title The title of the tab
     *
     * @author N7ghtm4r3 - Tecknobit
     */
    private data class NavigationTab(
        val tabIdentifier: String,
        val icon: ImageVector? = null,
        val title: StringResource
    )

}