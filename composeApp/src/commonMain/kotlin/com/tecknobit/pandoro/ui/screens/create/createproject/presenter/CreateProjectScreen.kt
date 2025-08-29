@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.create.createproject.presenter

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups3
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.screens.create.createproject.presentation.CreateProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.item.group.components.GroupLogos
import com.tecknobit.pandoro.ui.screens.item.group.components.GroupsProjectCandidate
import com.tecknobit.pandoro.ui.screens.lists.groups.data.Group
import com.tecknobit.pandoro.ui.screens.lists.projects.data.Project
import com.tecknobit.pandoro.ui.screens.create.CreateScreen
import com.tecknobit.pandoro.ui.screens.shared.presenters.PandoroScreen
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

/**
 * The [CreateProjectScreen] displays the form to create a new project or edit an existing one
 *
 * @param projectId The identifier of the project to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see CreateScreen
 */
class CreateProjectScreen(
    projectId: String?
) : CreateScreen<Project, CreateProjectScreenViewModel>(
    itemId = projectId,
    creationTitle = Res.string.create_project,
    editingTitle = Res.string.edit_project,
    viewModel = CreateProjectScreenViewModel(
        projectId = projectId
    )
) {

    /**
     * Method used to retrieve a [SessionFlowState] instance used by the inheritors screens
     *
     * @return the state instance as [SessionFlowState]
     */
    @OptIn(ExperimentalComposeApi::class)
    override fun sessionFlowState(): SessionFlowState {
        return viewModel.sessionFlowState
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
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
                state = modalBottomSheetState,
                scope = scope,
            )
        }
    }

    /**
     * [Form] displayed as card
     */
    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
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
                        pickerSize = 130.dp
                    )
                    ProjectCardFormDetails(
                        descriptionModifier = Modifier
                            .weight(1f)
                    )
                }
            }
        }
    }

    /**
     * The section where are displayed the details of the project
     *
     * @param descriptionModifier The modifier to apply to the [EquinoxOutlinedTextField] where the
     * description can be typed
     */
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
                value = viewModel.projectName,
                isError = viewModel.projectNameError,
                validator = { isValidProjectName(it) },
                label = Res.string.name,
                errorText = Res.string.wrong_name
            )
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = viewModel.projectVersion,
                isError = viewModel.projectVersionError,
                validator = { isValidVersion(it) },
                label = Res.string.version,
                errorText = Res.string.wrong_project_version
            )
        }
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = viewModel.projectRepository,
            isError = viewModel.projectRepositoryError,
            validator = { isValidRepository(it) },
            label = Res.string.project_repository,
            errorText = Res.string.wrong_repository_url
        )
        EquinoxOutlinedTextField(
            modifier = descriptionModifier
                .fillMaxWidth(),
            maxLines = Int.MAX_VALUE,
            value = viewModel.projectDescription,
            isError = viewModel.projectDescriptionError,
            validator = { isValidProjectDescription(it) },
            label = Res.string.description,
            errorText = Res.string.wrong_description
        )
        ProjectCardFormActions()
    }

    /**
     * The actions available in the [CardForm] to manage the create or edit operation
     */
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
                    viewModel.workOnProject()
                }
            }
        }
    }

    /**
     * Dedicated layout to manage the groups where the project is shared
     */
    @Composable
    @NonRestartableComposable
    private fun ManageProjectGroups() {
        if(viewModel.authoredGroups.isNotEmpty()) {
            val modalBottomSheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = viewModel.projectGroups.isEmpty(),
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
                visible = viewModel.projectGroups.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                GroupLogos(
                    groups = viewModel.projectGroups,
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
            ProjectGroups(
                state = modalBottomSheetState,
                scope = scope
            )
        }
    }

    /**
     * [Form] displayed as full screen object, this is used for example in the mobile devices
     */
    @Composable
    @RequiresSuperCall
    @CompactClassComponent
    @NonRestartableComposable
    override fun FullScreenForm() {
        super.FullScreenForm()
        Box {
            FullScreenProjectDetails()
            IconPicker(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                pickerSize = 120.dp
            )
        }
    }

    /**
     * The section where the user can fill the form with the project details
     *
     * description can be typed
     */
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
                    value = viewModel.projectName,
                    isError = viewModel.projectNameError,
                    validator = { isValidProjectName(it) },
                    label = Res.string.name,
                    errorText = Res.string.wrong_name,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                EquinoxOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.projectVersion,
                    isError = viewModel.projectVersionError,
                    validator = { isValidVersion(it) },
                    label = Res.string.version,
                    errorText = Res.string.wrong_project_version,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                EquinoxOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.projectRepository,
                    isError = viewModel.projectRepositoryError,
                    validator = { isValidRepository(it) },
                    label = Res.string.project_repository,
                    errorText = Res.string.wrong_repository_url,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                EquinoxOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = viewModel.projectDescription,
                    isError = viewModel.projectDescriptionError,
                    validator = { isValidProjectDescription(it) },
                    maxLines = 8,
                    label = Res.string.description,
                    errorText = Res.string.wrong_description,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
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
                    onClick = { viewModel.workOnProject() }
                )
            }
        }
    }

    /**
     * The logos of the groups where the project is shared, the component allows their management
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
    @Composable
    @NonRestartableComposable
    private fun ProjectGroups(
        state: SheetState,
        scope: CoroutineScope
    ) {
        val groups = remember { mutableListOf<Group>() }
        LaunchedEffect(Unit) {
            groups.addAll(viewModel.projectGroups + viewModel.authoredGroups)
        }
        GroupsProjectCandidate(
            state = state,
            scope = scope,
            groups = groups.distinctBy { group -> group.id }
        ) { group ->
            if (viewModel.authoredGroups.any { groupToCheck -> groupToCheck.id == group.id }) {
                var added by remember {
                    mutableStateOf(
                        viewModel.candidateGroups.contains(group.id) ||
                        viewModel.projectGroups.contains(group)
                    )
                }
                Checkbox(
                    checked = added,
                    onCheckedChange = { selected ->
                        viewModel.manageCandidateGroup(
                            group = group
                        )
                        added = selected
                    }
                )
            }
        }
    }

    /**
     * Picker to chose the icon of the project
     *
     * @param modifier The modifier to apply to the component
     * @param pickerSize The size of the picker
     */
    @Composable
    @NonRestartableComposable
    private fun IconPicker(
        modifier: Modifier = Modifier,
        pickerSize: Dp
    ) {
        ImagePicker(
            modifier = modifier,
            pickerSize = pickerSize,
            imageData = viewModel.projectIcon.value,
            contentDescription = "Project icon",
            onImagePicked = { icon ->
                icon?.let {
                    viewModel.projectIcon.value = icon.toString()
                    viewModel.projectIconPayload = icon
                }
            }
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveProject()
        viewModel.retrieveAuthoredGroups()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel.project.collectAsState()
        viewModel.projectNameError = remember { mutableStateOf(false) }
        viewModel.projectVersionError = remember { mutableStateOf(false) }
        viewModel.projectRepositoryError = remember { mutableStateOf(false) }
        viewModel.projectDescriptionError = remember { mutableStateOf(false) }
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

    @Composable
    override fun CollectStatesAfterLoading() {
        viewModel.projectIcon = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.icon
                else
                    ""
            )
        }
        viewModel.projectName = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.name
                else
                    ""
            )
        }
        viewModel.projectVersion = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.version
                else
                    ""
            )
        }
        viewModel.projectRepository = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.projectRepo
                else
                    ""
            )
        }
        viewModel.projectDescription = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.description
                else
                    ""
            )
        }
    }

}