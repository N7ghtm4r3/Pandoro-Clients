@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.creategroup.presenter

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandoro.ui.screens.creategroup.presentation.CreateGroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.group.components.GroupActions
import com.tecknobit.pandoro.ui.screens.group.components.GroupMembers
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.projects.data.Project
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupNameValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.add_projects
import pandoro.composeapp.generated.resources.create_group
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit_group
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.wrong_description
import pandoro.composeapp.generated.resources.wrong_name

/**
 * The [CreateGroupScreen] displays the form to create a new group or edit an existing one
 *
 * @param groupId The identifier of the group to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
 * @see PandoroScreen
 * @see CreateScreen
 */
class CreateGroupScreen(
    groupId: String?
): CreateScreen<Group, CreateGroupScreenViewModel>(
    itemId = groupId,
    viewModel = CreateGroupScreenViewModel(
        groupId = groupId
    )
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent(
            creationTitle = Res.string.create_group,
            editingTitle = Res.string.edit_group
        ) {
            viewModel.groupLogo = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.logo
                    else
                        ""
                )
            }
            viewModel.groupName = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.name
                    else
                        ""
                )
            }
            viewModel.groupDescription = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.description
                    else
                        ""
                )
            }
        }
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        if(fullScreenFormType.value) {
            GroupActions(
                viewModel = viewModel
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
                    LogoPicker(
                        pickerSize = 120.dp
                    )
                    Row (
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Column (
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            GroupDetails(
                                descriptionModifier = Modifier
                                    .weight(1f)
                            )
                        }
                        if(viewModel.candidatesMemberAvailable.value) {
                            GroupMembers(
                                modifier = Modifier
                                    .weight(1.3f),
                                viewModel = viewModel
                            )
                        }
                    }
                    GroupCardFormActions()
                }
            }
        }
    }

    /**
     * The actions available in the [CardForm] to manage the create or edit operation
     */
    @Composable
    @NonRestartableComposable
    private fun GroupCardFormActions() {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!isEditing || item.value!!.iAmAnAdmin())
                ManageGroupProjects()
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                SaveButton {
                    viewModel.workOnGroup()
                }
            }
        }
    }

    /**
     * Dedicated layout to manage the projects shared in the group
     */
    @Composable
    @NonRestartableComposable
    private fun ManageGroupProjects() {
        if(viewModel.userProjects.isNotEmpty()) {
            val sheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = viewModel.groupProjects.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            sheetState.show()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.add_projects)
                    )
                }
            }
            AnimatedVisibility(
                visible = viewModel.groupProjects.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProjectIcons(
                    projects = viewModel.groupProjects,
                    onClick = {
                        scope.launch {
                            sheetState.show()
                        }
                    }
                )
            }
            GroupProjects(
                state = sheetState,
                scope = scope
            )
        }
    }

    /**
     * The icons of the projects shared in the group, the component allows their management
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
    @Composable
    @NonRestartableComposable
    private fun GroupProjects(
        state: SheetState,
        scope: CoroutineScope
    ) {
        val projects: MutableList<Project> = remember { mutableListOf() }
        LaunchedEffect(Unit) {
            projects.addAll(viewModel.userProjects + viewModel.groupProjects)
        }
        GroupProjectsCandidate(
            state = state,
            scope = scope,
            projects = projects.distinctBy { project -> project.id },
            trailingContent = { project ->
                if (viewModel.userProjects.any { checkProject -> checkProject.id == project.id }) {
                    var added by remember {
                        mutableStateOf(
                            viewModel.candidateProjects.contains(project.id) ||
                            viewModel.groupProjects.contains(project)
                        )
                    }
                    Checkbox(
                        checked = added,
                        onCheckedChange = { selected ->
                            viewModel.manageProjectCandidate(
                                project = project
                            )
                            added = selected
                        }
                    )
                }
            }
        )
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
                    GroupDetails()
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
                        onClick = { viewModel.workOnGroup() }
                    )
                }
            }
            LogoPicker(
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    ),
                pickerSize = 120.dp
            )
        }
    }

    /**
     * The section where the user can fill the form with the group details
     *
     * @param descriptionModifier The modifier to apply to the [EquinoxOutlinedTextField] where the
     * description can be typed
     */
    @Composable
    @NonRestartableComposable
    private fun GroupDetails(
        descriptionModifier: Modifier = Modifier
    ) {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = viewModel.groupName,
            isError = viewModel.groupNameError,
            validator = { isGroupNameValid(it) },
            label = Res.string.name,
            errorText = Res.string.wrong_name,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        EquinoxOutlinedTextField(
            modifier = descriptionModifier
                .fillMaxWidth(),
            maxLines = Int.MAX_VALUE,
            value = viewModel.groupDescription,
            isError = viewModel.groupDescriptionError,
            validator = { isGroupDescriptionValid(it) },
            label = Res.string.description,
            errorText = Res.string.wrong_description,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    /**
     * Picker to chose the logo of the group
     *
     * @param modifier The modifier to apply to the component
     * @param pickerSize The size of the picker
     */
    @Composable
    @NonRestartableComposable
    private fun LogoPicker(
        modifier: Modifier = Modifier,
        pickerSize: Dp
    ) {
        ImagePicker(
            modifier = modifier,
            pickerSize = pickerSize,
            imageData = viewModel.groupLogo.value,
            contentDescription = "Group Logo",
            onImagePicked = { logo ->
                // TODO: TO SET
                /*
                viewModel.groupLogo.value = getImagePath(
                    imagePic = logo
                )*/
            }
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveGroup()
        viewModel.retrieveUserProjects()
        viewModel.countCandidatesMember()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel.group.collectAsState()
        viewModel.groupNameError = remember { mutableStateOf(false) }
        viewModel.groupDescriptionError = remember { mutableStateOf(false) }
    }

}