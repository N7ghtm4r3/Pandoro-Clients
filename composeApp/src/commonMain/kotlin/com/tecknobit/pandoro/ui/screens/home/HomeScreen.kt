package com.tecknobit.pandoro.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Medium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.icons.Activity
import com.tecknobit.pandoro.ui.screens.groups.presenter.GroupsScreen
import com.tecknobit.pandoro.ui.screens.notes.presenter.NotesScreen
import com.tecknobit.pandoro.ui.screens.overview.OverviewScreen
import com.tecknobit.pandoro.ui.screens.profile.ProfileScreen
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

class HomeScreen: EquinoxScreen<EquinoxViewModel>() {

    companion object {

        const val PROJECTS_SCREEN = "ProjectsScreen"

        const val NOTES_SCREEN = "NotesScreen"

        const val OVERVIEW_SCREEN = "OverviewScreen"

        const val GROUPS_SCREEN = "GroupsScreen"

        const val PROFILE_SCREEN = "ProfileScreen"

        private val destinations = listOf(
            NavigationTab(
                tabIdentifier = PROJECTS_SCREEN,
                icon = Icons.Default.Folder,
                title = Res.string.projects
            ),
            NavigationTab(
                tabIdentifier = NOTES_SCREEN,
                icon = Icons.AutoMirrored.Filled.Notes,
                title = Res.string.notes
            ),
            NavigationTab(
                tabIdentifier = OVERVIEW_SCREEN,
                icon = Activity,
                title = Res.string.overview
            ),
            NavigationTab(
                tabIdentifier = GROUPS_SCREEN,
                icon = Icons.Default.Groups3,
                title = Res.string.groups
            ),
            NavigationTab(
                tabIdentifier = PROFILE_SCREEN,
                title = Res.string.profile
            )
        )

        private var currentScreenDisplayed: Int = 0

        lateinit var isBottomNavigationMode: MutableState<Boolean>

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

    private lateinit var currentDestination: MutableState<NavigationTab>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    @ExperimentalMaterial3WindowSizeClassApi
    override fun ArrangeScreenContent() {
        val widthSizeClass = getCurrentWidthSizeClass()
        Box (
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
            when(widthSizeClass) {
                Expanded, Medium -> SideNavigationBar()
                else -> {
                    BottomNavigationBar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun SideNavigationBar() {
        isBottomNavigationMode.value = false
        NavigationRail(
            modifier = Modifier
                .width(125.dp),
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            header = {
                Thumbnail(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        )
                        .clip(CircleShape)
                        .clickable { currentDestination.value = destinations.last() },
                    size = 75.dp,
                    // TODO: TO SET THE USER PIC
                    thumbnailData = "https://t4.ftcdn.net/jpg/03/86/82/73/360_F_386827376_uWOOhKGk6A4UVL5imUBt20Bh8cmODqzx.jpg",
                    contentDescription = "Profile pic"
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

    @Composable
    @NonRestartableComposable
    private fun BottomNavigationBar(
        modifier: Modifier = Modifier
    ) {
        isBottomNavigationMode.value = true
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

    @Composable
    @NonRestartableComposable
    private fun NavigationTab.TabIcon() {
        val icon = this.icon
        if(icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        } else {
            // TODO: TO SET THE USER PIC
            Thumbnail(
                thumbnailData = "https://t4.ftcdn.net/jpg/03/86/82/73/360_F_386827376_uWOOhKGk6A4UVL5imUBt20Bh8cmODqzx.jpg",
                contentDescription = "Profile pic"
            )
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        currentDestination = remember { mutableStateOf(destinations[currentScreenDisplayed]) }
        isBottomNavigationMode = remember { mutableStateOf(false) }
    }

    private data class NavigationTab(
        val tabIdentifier: String,
        val icon: ImageVector? = null,
        val title: StringResource
    )

}