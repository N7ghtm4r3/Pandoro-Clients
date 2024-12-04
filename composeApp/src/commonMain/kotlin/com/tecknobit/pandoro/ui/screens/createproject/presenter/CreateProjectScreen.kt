@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.createproject.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.createproject.presentation.CreateProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.group.components.GroupIcons
import com.tecknobit.pandoro.ui.screens.group.components.GroupsProjectCandidate
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectName
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidRepository
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.choose_the_icon_of_the_project
import pandoro.composeapp.generated.resources.create_project
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit
import pandoro.composeapp.generated.resources.edit_project
import pandoro.composeapp.generated.resources.logo
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.project_repository
import pandoro.composeapp.generated.resources.save
import pandoro.composeapp.generated.resources.share_project
import pandoro.composeapp.generated.resources.version
import pandoro.composeapp.generated.resources.wrong_description
import pandoro.composeapp.generated.resources.wrong_name
import pandoro.composeapp.generated.resources.wrong_project_version
import pandoro.composeapp.generated.resources.wrong_repository_url

class CreateProjectScreen(
    projectId: String?
) : PandoroScreen<CreateProjectScreenViewModel>(
    viewModel = CreateProjectScreenViewModel(
        projectId = projectId
    )
) {

    private val isEditing: Boolean = projectId != null

    private lateinit var project: State<Project?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        PandoroTheme {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.primary,
                snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) }
            ) {
                PlaceContent(
                    navBackAction = { navigator.goBack() },
                    screenTitle = if(isEditing)
                        Res.string.edit_project
                    else
                        Res.string.create_project
                ) {
                    ProjectForm()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectForm() {
        val windowWidthSizeClass = getCurrentWidthSizeClass()
        when(windowWidthSizeClass) {
            Expanded -> ProjectCardForm()
            else -> FullScreenProjectForm()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectCardForm() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(
                        width = 750.dp,
                        height = 550.dp
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconPicker(
                        iconSize = 130.dp
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        EquinoxOutlinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            value = viewModel!!.projectName,
                            isError = viewModel!!.projectNameError,
                            validator = { isValidProjectName(it) },
                            label = Res.string.name,
                            errorText = Res.string.wrong_name
                        )
                        EquinoxOutlinedTextField(
                            modifier = Modifier
                                .weight(1f),
                            value = viewModel!!.projectVersion,
                            isError = viewModel!!.projectVersionError,
                            validator = { isValidVersion(it) },
                            label = Res.string.version,
                            errorText = Res.string.wrong_project_version
                        )
                    }
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = viewModel!!.projectRepository,
                        isError = viewModel!!.projectRepositoryError,
                        validator = { isValidRepository(it) },
                        label = Res.string.project_repository,
                        errorText = Res.string.wrong_repository_url
                    )
                    EquinoxOutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        maxLines = Int.MAX_VALUE,
                        value = viewModel!!.projectDescription,
                        isError = viewModel!!.projectDescriptionError,
                        validator = { isValidProjectDescription(it) },
                        label = Res.string.description,
                        errorText = Res.string.wrong_description
                    )
                    ProjectCardFormActions()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectCardFormActions() {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProjectGroups()
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {
                        viewModel!!.workOnProject()
                    }
                ) {
                    Text(
                        text = stringResource(
                            if(isEditing)
                                Res.string.edit
                            else
                                Res.string.save
                        )
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectGroups() {
        if(viewModel!!.groupAdministratedByUser.isNotEmpty()) {
            val modalBottomSheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = viewModel!!.projectsGroups.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.share_project)
                    )
                }
            }
            AnimatedVisibility(
                visible = viewModel!!.projectsGroups.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {

                GroupIcons(
                    groups = viewModel!!.groupAdministratedByUser,
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
            GroupsProjectCandidate(
                state = modalBottomSheetState,
                scope = scope,
                groups = viewModel!!.groupAdministratedByUser,
                trailingContent = { group ->
                    var added by remember {
                        mutableStateOf(viewModel!!.candidateGroups.contains(group.id))
                    }
                    Checkbox(
                        checked = added,
                        onCheckedChange = { selected ->
                            viewModel!!.manageCandidateGroup(
                                group = group,
                                added = selected
                            )
                            added = selected
                        }
                    )
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun IconPicker(
        iconSize: Dp
    ) {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single,
            title = stringResource(Res.string.choose_the_icon_of_the_project)
        ) { image ->
            viewModel!!.projectIcon.value = getImagePath(
                imagePic = image
            )
        }
        Column (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                AsyncImage(
                    modifier = Modifier
                        .size(iconSize)
                        .clip(CircleShape)
                        .align(Alignment.Center),
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(viewModel!!.projectIcon.value)
                        .crossfade(true)
                        .crossfade(500)
                        .build(),
                    // TODO: TO SET
                    //imageLoader = imageLoader,
                    contentDescription = "Project icon",
                    contentScale = ContentScale.Crop,
                    error = painterResource(Res.drawable.logo)
                )
                IconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(Color(0xD0DFD8D8))
                        .size(40.dp),
                    onClick = { launcher.launch() }
                ) {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = null
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FullScreenProjectForm() {

    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
        viewModel!!.retrieveGroupsWhereUserIsAnAdmin()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        project = viewModel!!.project.collectAsState()
        viewModel!!.projectIcon = remember {
            mutableStateOf(
                if(isEditing)
                    project.value!!.icon
                else
                    ""
            )
        }
        viewModel!!.projectName = remember {
            mutableStateOf(
                if(isEditing)
                    project.value!!.name
                else
                    ""
            )
        }
        viewModel!!.projectNameError = remember { mutableStateOf(false) }
        viewModel!!.projectVersion = remember {
            mutableStateOf(
                if(isEditing)
                    project.value!!.version
                else
                    ""
            )
        }
        viewModel!!.projectVersionError = remember { mutableStateOf(false) }
        viewModel!!.projectRepository = remember {
            mutableStateOf(
                if(isEditing)
                    project.value!!.projectRepo
                else
                    ""
            )
        }
        viewModel!!.projectRepositoryError = remember { mutableStateOf(false) }
        viewModel!!.projectDescription = remember {
            mutableStateOf(
                if(isEditing)
                    project.value!!.description
                else
                    ""
            )
        }
        viewModel!!.projectDescriptionError = remember { mutableStateOf(false) }
    }

}