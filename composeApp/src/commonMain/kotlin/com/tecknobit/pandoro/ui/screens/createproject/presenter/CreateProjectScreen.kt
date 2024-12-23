@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.createproject.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.ui.screens.createproject.presentation.CreateProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.group.components.GroupIcons
import com.tecknobit.pandoro.ui.screens.group.components.GroupsProjectCandidate
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectDescription
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidProjectName
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidRepository
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.create_project
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit_project
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.project_repository
import pandoro.composeapp.generated.resources.share_project
import pandoro.composeapp.generated.resources.version
import pandoro.composeapp.generated.resources.wrong_description
import pandoro.composeapp.generated.resources.wrong_name
import pandoro.composeapp.generated.resources.wrong_project_version
import pandoro.composeapp.generated.resources.wrong_repository_url

class CreateProjectScreen(
    projectId: String?
) : CreateScreen<Project, CreateProjectScreenViewModel>(
    itemId = projectId,
    viewModel = CreateProjectScreenViewModel(
        projectId = projectId
    )
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent(
            creationTitle = Res.string.create_project,
            editingTitle = Res.string.edit_project
        ) {
            viewModel!!.projectIcon = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.icon
                    else
                        ""
                )
            }
            viewModel!!.projectName = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.name
                    else
                        ""
                )
            }
            viewModel!!.projectVersion = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.version
                    else
                        ""
                )
            }
            viewModel!!.projectRepository = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.projectRepo
                    else
                        ""
                )
            }
            viewModel!!.projectDescription = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.description
                    else
                        ""
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        val modalBottomSheetState = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        if (fullScreenFormType.value) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        modalBottomSheetState.show()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Groups3,
                    contentDescription = null
                )
            }
            ProjectGroups(
                modalBottomSheetState = modalBottomSheetState,
                scope = scope,
            )
        }
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun CardForm() {
        super.CardForm()
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(
                        width = FORM_CARD_WIDTH,
                        height = FORM_CARD_HEIGHT
                    )
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconPicker(
                        iconSize = 130.dp
                    )
                    ProjectCardFormDetails(
                        descriptionModifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectCardFormDetails(
        descriptionModifier: Modifier
    ) {
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
            modifier = descriptionModifier
                .fillMaxWidth(),
            maxLines = Int.MAX_VALUE,
            value = viewModel!!.projectDescription,
            isError = viewModel!!.projectDescriptionError,
            validator = { isValidProjectDescription(it) },
            label = Res.string.description,
            errorText = Res.string.wrong_description
        )
        ProjectCardFormActions()
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectCardFormActions() {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ManageProjectGroups()
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                SaveButton {
                    viewModel!!.workOnProject()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ManageProjectGroups() {
        if(viewModel!!.authoredGroups.isNotEmpty()) {
            val modalBottomSheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = viewModel!!.projectGroups.isEmpty(),
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
                visible = viewModel!!.projectGroups.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GroupIcons(
                    groups = viewModel!!.projectGroups,
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
            ProjectGroups(
                modalBottomSheetState = modalBottomSheetState,
                scope = scope
            )
        }
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun FullScreenForm() {
        super.FullScreenForm()
        Box {
            FullScreenProjectDetails()
            IconPicker(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                iconSize = 120.dp
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun FullScreenProjectDetails() {
        Card(
            modifier = Modifier
                .padding(
                    top = 80.dp
                ),
            shape = RoundedCornerShape(
                topStart = 50.dp,
                topEnd = 50.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 60.dp,
                        start = 25.dp,
                        end = 25.dp,
                        bottom = 25.dp
                    )
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                EquinoxOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel!!.projectName,
                    isError = viewModel!!.projectNameError,
                    validator = { isValidProjectName(it) },
                    label = Res.string.name,
                    errorText = Res.string.wrong_name
                )
                EquinoxOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel!!.projectVersion,
                    isError = viewModel!!.projectVersionError,
                    validator = { isValidVersion(it) },
                    label = Res.string.version,
                    errorText = Res.string.wrong_project_version
                )
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
                        .fillMaxWidth(),
                    value = viewModel!!.projectDescription,
                    isError = viewModel!!.projectDescriptionError,
                    validator = { isValidProjectDescription(it) },
                    maxLines = 8,
                    label = Res.string.description,
                    errorText = Res.string.wrong_description
                )
                SaveButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(
                        size = 8.dp
                    ),
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    onClick = { viewModel!!.workOnProject() }
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProjectGroups(
        modalBottomSheetState: SheetState,
        scope: CoroutineScope
    ) {
        GroupsProjectCandidate(
            state = modalBottomSheetState,
            scope = scope,
            groups = remember { viewModel!!.projectGroups + viewModel!!.authoredGroups }
        ) { group ->
            if (viewModel!!.authoredGroups.contains(group)) {
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
        }
    }

    @Composable
    @NonRestartableComposable
    private fun IconPicker(
        modifier: Modifier = Modifier,
        iconSize: Dp
    ) {
        ImagePicker(
            modifier = modifier,
            iconSize = iconSize,
            imageData = viewModel!!.projectIcon.value,
            contentDescription = "Project icon",
            onImagePicked = { image ->
                viewModel!!.projectIcon.value = getImagePath(
                    imagePic = image
                )
            }
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveProject()
        viewModel!!.retrieveAuthoredGroups()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel!!.project.collectAsState()
        viewModel!!.projectNameError = remember { mutableStateOf(false) }
        viewModel!!.projectVersionError = remember { mutableStateOf(false) }
        viewModel!!.projectRepositoryError = remember { mutableStateOf(false) }
        viewModel!!.projectDescriptionError = remember { mutableStateOf(false) }
    }

}