package com.tecknobit.pandoro.ui.screens.home.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
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

        private const val PROFILE_TAB_INDEX = 4

    }

    private val viewModel = HomeScreenViewModel()

    /**
     * `unreadChangelogs` the state of the unread changelogs number
     */
    private lateinit var unreadChangelogs: State<Int>

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
    }

    @Composable
    override fun ColumnScope.SideNavigationHeaderContent() {
        ProfilePic(
            modifier = Modifier
                .padding(
                    top = 16.dp
                ),
            size = 100.dp,
            onClick = { activeNavigationTabIndex.value = PROFILE_TAB_INDEX }
        )
    }

    @Composable
    override fun ColumnScope.SideNavigationFooterContent() {
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "v" + stringResource(Res.string.app_version),
            fontFamily = displayFontFamily,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }

    @Composable
    override fun SideNavigationItem(
        index: Int,
        tab: I18nNavigationTab,
    ) {
        if(index != PROFILE_TAB_INDEX)
            super.SideNavigationItem(index, tab)
    }

    @Composable
    override fun RowScope.BottomNavigationItem(
        index: Int,
        tab: I18nNavigationTab,
    ) {
        NavigationBarItem(
            selected = index == activeNavigationTabIndex.value,
            onClick = { activeNavigationTabIndex.value = index },
            icon = {
                if(index != PROFILE_TAB_INDEX) {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.contentDescription
                    )
                } else
                    ProfilePic()
            },
            alwaysShowLabel = false,
            colors = bottomNavigationItemColors(),
            label = {
                if(index != PROFILE_TAB_INDEX) {
                    Text(
                        text = tab.prepareTitle(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        )
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
                AnimatedVisibility(
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

    override fun onCreate() {
        super.onCreate()
        viewModel.setActiveContext(this::class)
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

}