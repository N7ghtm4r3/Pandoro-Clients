@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.project.presenter

import CalendarPlus
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AreaChart
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.SCHEDULE_UPDATE_SCREEN
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentSizeClass
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteProject
import com.tecknobit.pandoro.ui.screens.group.components.GroupIcons
import com.tecknobit.pandoro.ui.screens.project.components.ModalProjectStats
import com.tecknobit.pandoro.ui.screens.project.components.ProjectsStats
import com.tecknobit.pandoro.ui.screens.project.components.UpdateCard
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.asText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.toColor
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.screens.ItemScreen
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import com.tecknobit.pandorocore.enums.UpdateStatus
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.github
import pandoro.composeapp.generated.resources.gitlab
import pandoro.composeapp.generated.resources.no_updates_scheduled
import pandoro.composeapp.generated.resources.stats
import pandoro.composeapp.generated.resources.updates

class ProjectScreen(
    projectId: String,
    private val updateToExpandId: String?
) : ItemScreen<Project, ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun ItemTitle() {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            ScreenTitle(
                navBackAction = { navigator.goBack() },
                title = item.value!!.name
            )
            item.value!!.getRepositoryPlatform()?.let { platform ->
                val uriHandler = LocalUriHandler.current
                IconButton(
                    onClick =  {
                        uriHandler.openUri(
                            uri = item.value!!.projectRepo
                        )
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .then(
                                when(platform) {
                                    RepositoryPlatform.Github -> Modifier
                                    RepositoryPlatform.GitLab -> Modifier
                                        .padding(
                                            top = 10.dp
                                        )
                                        .size(30.dp)
                                }
                            ),
                        painter = painterResource(
                            when(platform) {
                                RepositoryPlatform.Github -> Res.drawable.github
                                RepositoryPlatform.GitLab -> Res.drawable.gitlab
                            }
                        ),
                        contentDescription = "Platform Icon"
                    )
                }
            }
        }
    }

    override fun getThumbnailData(): String? {
        return item.value!!.icon
    }

    @Composable
    @NonRestartableComposable
    override fun ItemRelationshipItems() {
        Column {
            GroupIcons(
                modifier = Modifier
                    .heightIn(
                        min = 30.dp
                    ),
                project = item.value!!
            )
            Text(
                text = item.value!!.version.asVersionText(),
                fontSize = 16.sp
            )
        }
    }

    override fun getItemDescription(): String {
        return item.value!!.description
    }

    override fun onEdit() {
        navigator.navigate("$CREATE_PROJECT_SCREEN/${item.value!!.id}")
    }

    @Composable
    @NonRestartableComposable
    override fun DeleteItemAction(
        delete: MutableState<Boolean>
    ) {
        DeleteProject(
            viewModel = viewModel!!,
            project = item.value!!,
            show = delete,
            deleteRequest = { project ->
                viewModel!!.deleteProject(
                    project = project,
                    onDelete = {
                        delete.value = false
                        navigator.goBack()
                    }
                )
            }
        )
    }

    override fun getItemAuthor(): PandoroUser {
        return item.value!!.author
    }

    @Composable
    @NonRestartableComposable
    override fun ScreenContent() {
        val windowSizeClass = getCurrentSizeClass()
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass
        when {
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Expanded -> {
                UpdatesStatsSection()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Medium -> {
                ProjectUpdatesSection()
            }
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Medium -> {
                UpdatesStatsSection()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded -> {
                ProjectUpdatesSection()
            }
            else -> ProjectUpdatesSection()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun UpdatesStatsSection() {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                ProjectUpdatesSection()
            }
            Column (
                modifier = Modifier
                    .weight(1f)
            ) {
                val publishedUpdates = item.value!!.getPublishedUpdates()
                AnimatedVisibility(
                    visible = publishedUpdates.isNotEmpty()
                ) {
                    Section(
                        modifier = Modifier
                            .padding(
                                end = 16.dp
                            )
                            .fillMaxSize(),
                        header = Res.string.stats
                    ) {
                        ProjectsStats(
                            project = item.value!!,
                            publishedUpdates = publishedUpdates
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectUpdatesSection() {
        Section(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .widthIn(
                    max = 1000.dp
                )
                .fillMaxSize(),
            header = Res.string.updates,
            filtersContent = { Filters() }
        ) {
            PaginatedLazyColumn(
                modifier = Modifier
                    .animateContentSize(),
                paginationState = viewModel!!.updatesState,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                firstPageEmptyIndicator = {
                    EmptyListUI(
                        icon = CalendarPlus,
                        subText = Res.string.no_updates_scheduled,
                        textStyle = TextStyle(
                            fontFamily = bodyFontFamily
                        ),
                        themeColor = MaterialTheme.colorScheme.inversePrimary
                    )
                }
                // TODO: TO SET
                /*firstPageProgressIndicator = { ... },
                newPageProgressIndicator = { ... },*/
                /*firstPageErrorIndicator = { e -> // from setError
                    ... e.message ...
                    ... onRetry = { paginationState.retryLastFailedRequest() } ...
                },
                newPageErrorIndicator = { e -> ... },
                // The rest of LazyColumn params*/
            ) {
                items(
                    items = viewModel!!.updatesState.allItems!!,
                    key = { update -> update.id }
                ) { update ->
                    UpdateCard(
                        viewModel = viewModel!!,
                        project = item.value!!,
                        update = update,
                        viewChangeNotesFlag = updateToExpandId != null && update.id == updateToExpandId
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Filters() {
        var menuOpened by remember { mutableStateOf(false) }
        val areFiltersSet = viewModel!!.areFiltersSet()
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = {
                if(areFiltersSet)
                    viewModel!!.clearFilters()
                else
                    menuOpened = true
            }
        ) {
            Icon(
                imageVector = if(areFiltersSet)
                    Icons.Default.FilterListOff
                else
                    Icons.Default.FilterList,
                contentDescription = null
            )
        }
        DropdownMenu(
            expanded = menuOpened,
            onDismissRequest = { menuOpened = false }
        ) {
            UpdateStatus.entries.forEach { status ->
                var selected by remember {
                    mutableStateOf(viewModel!!.updateStatusesFilters.contains(status))
                }
                DropdownMenuItem(
                    modifier = Modifier
                        .colorOneSideBorder(
                            borderToColor = BorderToColor.START,
                            width = 5.dp,
                            color = status.toColor()
                        ),
                    text = {
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = status.asText()
                            )
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.End
                            ) {
                                Checkbox(
                                    checked = selected,
                                    onCheckedChange = {
                                        selected = it
                                        viewModel!!.manageStatusesFilter(
                                            selected = selected,
                                            updateStatus = status
                                        )
                                    }
                                )
                            }
                        }
                    },
                    onClick = {
                        selected = !selected
                        viewModel!!.manageStatusesFilter(
                            selected = selected,
                            updateStatus = status
                        )
                    }
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        val windowSizeClass = getCurrentSizeClass()
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass
        when {
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Expanded -> {
                ScheduleButton()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Medium -> {
                FabButtons()
            }
            widthClass == Expanded && heightClass == WindowHeightSizeClass.Medium -> {
                ScheduleButton()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded -> {
                FabButtons()
            }
            else -> FabButtons()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FabButtons() {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            ViewStatsButton()
            ScheduleButton()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ViewStatsButton() {
        val publishedUpdates = item.value!!.getPublishedUpdates()
        AnimatedVisibility(
            visible = publishedUpdates.isNotEmpty()
        ) {
            val state = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
            val scope = rememberCoroutineScope()
            SmallFloatingActionButton(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                onClick = {
                    scope.launch {
                        state.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.AreaChart,
                    contentDescription = null
                )
            }
            ModalProjectStats(
                state = state,
                scope = scope,
                project = item.value!!,
                publishedUpdates = publishedUpdates
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ScheduleButton() {
        FloatingActionButton(
            onClick = {
                navigator.navigate(
                    route = "$SCHEDULE_UPDATE_SCREEN/${item.value!!.id}/${item.value!!.name}"
                )
            }
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        item = viewModel!!.project.collectAsState()
        viewModel!!.updateStatusesFilters = remember { UpdateStatus.entries.toMutableStateList() }
    }

}