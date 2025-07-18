@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class,
    ExperimentalComposeApi::class
)

package com.tecknobit.pandoro.ui.screens.project.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EmptyState
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.ExpandedClassComponent
import com.tecknobit.equinoxcompose.utilities.LayoutCoordinator
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.COMPACT_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcore.annotations.Returner
import com.tecknobit.pandoro.CREATE_PROJECT_SCREEN
import com.tecknobit.pandoro.SCHEDULE_UPDATE_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteProject
import com.tecknobit.pandoro.ui.screens.group.components.GroupLogos
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
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.theme.EmptyStateTitleStyle
import com.tecknobit.pandorocore.enums.RepositoryPlatform
import com.tecknobit.pandorocore.enums.UpdateStatus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.github
import pandoro.composeapp.generated.resources.gitlab
import pandoro.composeapp.generated.resources.no_updates
import pandoro.composeapp.generated.resources.no_updates_scheduled
import pandoro.composeapp.generated.resources.stats
import pandoro.composeapp.generated.resources.updates

/**
 * The [ProjectScreen] displays the details of a project and allow to manage that project
 *
 * @param projectId The identifier of the project to display
 * @param updateToExpandId If not null will expand the related change notes of the update requested
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see ItemScreen
 */
class ProjectScreen(
    projectId: String,
    private val updateToExpandId: String?
) : ItemScreen<Project, ProjectScreenViewModel>(
    viewModel = ProjectScreenViewModel(
        projectId = projectId
    )
) {

    /**
     * Method used to retrieve a [SessionFlowState] instance used by the inheritors screens
     *
     * @return the state instance as [SessionFlowState]
     */
    @Returner
    override fun sessionFlowState(): SessionFlowState {
        return viewModel.sessionFlowState
    }

    /**
     * The related content of the screen
     */
    @Composable
    @LayoutCoordinator
    override fun ItemContent() {
        ResponsiveContent(
            onExpandedSizeClass = { UpdatesStatsSection() },
            onMediumSizeClass = { ProjectUpdatesSection() },
            onCompactSizeClass = { ProjectUpdatesSection() }
        )
    }

    /**
     * The title of the screen
     */
    @Composable
    @ScreenSection
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

    /**
     * Method to get the thumbnail data from the item
     *
     * @return the thumbnail data of the item as nullable [String]
     */
    override fun getThumbnailData(): String? {
        return item.value!!.icon
    }

    /**
     * The related items of the [item] such groups or projects
     */
    @Composable
    override fun ItemRelationshipItems() {
        Column {
            GroupLogos(
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

    /**
     * Method to get the description of the item
     *
     * @return the description of the item as [String]
     */
    override fun getItemDescription(): String {
        return item.value!!.description
    }

    /**
     * The action to execute when the [item] has been edited
     */
    override fun onEdit() {
        navigator.navigate("$CREATE_PROJECT_SCREEN/${item.value!!.id}")
    }

    /**
     * The action to execute when the [item] has been requested to delete
     *
     * @param delete Whether the warn alert about the deletion is shown
     */
    @Composable
    @NonRestartableComposable
    override fun DeleteItemAction(
        delete: MutableState<Boolean>
    ) {
        DeleteProject(
            viewModel = viewModel,
            project = item.value!!,
            show = delete,
            deleteRequest = { project ->
                viewModel.deleteProject(
                    project = project,
                    onDelete = {
                        delete.value = false
                        navigator.goBack()
                    },
                    onFailure = {
                        viewModel.showSnackbarMessage(it)
                    }
                )
            }
        )
    }

    /**
     * Method to get the author of the [item]
     *
     * @return the author of the item as [PandoroUser]
     */
    override fun getItemAuthor(): PandoroUser {
        return item.value!!.author
    }

    /**
     * The statistics about the updates of the project
     */
    @Composable
    @ExpandedClassComponent
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

    /**
     * The updates of the project displayed in the related section where can be displayed and managed
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
    )
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
            val updates = viewModel.arrangeUpdatesList()
            if(updates.isNotEmpty()) {
                LazyColumn (
                    modifier = Modifier
                        .animateContentSize(),
                    contentPadding = PaddingValues(
                        bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = item.value!!.updates,
                        key = { update -> update.id }
                    ) { update ->
                        UpdateCard(
                            viewModel = viewModel,
                            project = item.value!!,
                            update = update,
                            viewChangeNotesFlag = updateToExpandId != null && update.id == updateToExpandId
                        )
                    }
                }
            } else {
                EmptyState(
                    containerModifier = Modifier
                        .fillMaxSize(),
                    resource = Res.drawable.no_updates,
                    resourceSize = responsiveAssignment(
                        onExpandedSizeClass = { 350.dp },
                        onMediumSizeClass = { 300.dp },
                        onCompactSizeClass = { 275.dp }
                    ),
                    contentDescription = "No updates scheduled",
                    title = stringResource(Res.string.no_updates_scheduled),
                    titleStyle = EmptyStateTitleStyle
                )
            }
        }
    }

    /**
     * Section to allowing the user to filters the [ProjectUpdatesSection]
     */
    @Composable
    @ScreenSection
    private fun Filters() {
        var menuOpened by remember { mutableStateOf(false) }
        val areFiltersSet = viewModel.areFiltersSet()
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = {
                if(areFiltersSet)
                    viewModel.clearFilters()
                else
                    menuOpened = true
            }
        ) {
            Icon(
                imageVector = if(areFiltersSet)
                    Icons.Default.FilterListOff
                else
                    Icons.Default.FilterList,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(
            expanded = menuOpened,
            onDismissRequest = { menuOpened = false }
        ) {
            UpdateStatus.entries.forEach { status ->
                var selected by remember {
                    mutableStateOf(viewModel.updateStatusesFilters.contains(status))
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
                                        viewModel.manageStatusesFilter(
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
                        viewModel.manageStatusesFilter(
                            selected = selected,
                            updateStatus = status
                        )
                    }
                )
            }
        }
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @LayoutCoordinator
    override fun FabAction() {
        ResponsiveContent(
            onExpandedSizeClass = { ScheduleButton() },
            onMediumSizeClass = { FabButtons() },
            onCompactSizeClass = { FabButtons() }
        )
    }

    /**
     * Custom [Column] container of the [ViewStatsButton] and [ScheduleButton]
     */
    @Composable
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [MEDIUM_CONTENT, COMPACT_CONTENT]
    )
    private fun FabButtons() {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            ViewStatsButton()
            ScheduleButton()
        }
    }

    /**
     * Custom [SmallFloatingActionButton] to view the statistic about the project
     */
    @Composable
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

    /**
     * Button to navigate to the [SCHEDULE_UPDATE_SCREEN] to schedule a new update for the
     * project
     */
    @Composable
    @ExpandedClassComponent
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

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveProject()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel.sessionFlowState = rememberSessionFlowState()
        item = viewModel.project.collectAsState()
        viewModel.updateStatusesFilters = remember { UpdateStatus.entries.toMutableStateList() }
    }

}