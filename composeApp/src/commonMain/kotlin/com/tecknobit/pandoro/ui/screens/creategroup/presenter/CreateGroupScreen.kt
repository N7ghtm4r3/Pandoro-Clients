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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.ui.screens.creategroup.presentation.CreateGroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.group.components.GroupActions
import com.tecknobit.pandoro.ui.screens.group.components.GroupMembers
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
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
            viewModel!!.groupLogo = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.logo
                    else
                        ""
                )
            }
            viewModel!!.groupName = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.name
                    else
                        ""
                )
            }
            viewModel!!.groupDescription = remember {
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
        if(fullScreenFormType.value) {
            GroupActions(
                viewModel = viewModel!!
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
                    LogoPicker(
                        iconSize = 120.dp
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
                        if(viewModel!!.candidateMembersAvailable.value) {
                            GroupMembers(
                                modifier = Modifier
                                    .weight(1.3f),
                                viewModel = viewModel!!
                            )
                        }
                    }
                    GroupCardFormActions()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupCardFormActions() {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // TODO: CHECK IF THE localUser IS AN ADMIN OF THE GROUP
            ManageGroupProjects()
            Column (
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                SaveButton {
                    viewModel!!.workOnGroup()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ManageGroupProjects() {
        if(viewModel!!.userProjects.isNotEmpty()) {
            val modalBottomSheetState = rememberModalBottomSheetState()
            val scope = rememberCoroutineScope()
            AnimatedVisibility(
                visible = viewModel!!.groupProjects.isEmpty(),
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
                        text = stringResource(Res.string.add_projects)
                    )
                }
            }
            AnimatedVisibility(
                visible = viewModel!!.groupProjects.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProjectIcons(
                    projects = viewModel!!.groupProjects,
                    onClick = {
                        scope.launch {
                            modalBottomSheetState.show()
                        }
                    }
                )
            }
            GroupProjects(
                modalBottomSheetState = modalBottomSheetState,
                scope = scope
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupProjects(
        modalBottomSheetState: SheetState,
        scope: CoroutineScope
    ) {
        GroupProjectsCandidate(
            modalBottomSheetState = modalBottomSheetState,
            scope = scope,
            projects = remember { viewModel!!.userProjects + viewModel!!.groupProjects },
            trailingContent = { project ->
                if (viewModel!!.userProjects.contains(project)) {
                    var added by remember {
                        mutableStateOf(viewModel!!.candidateProjects.contains(project.id))
                    }
                    Checkbox(
                        checked = added,
                        onCheckedChange = { selected ->
                            viewModel!!.manageProjectCandidate(
                                project = project,
                                added = selected
                            )
                            added = selected
                        }
                    )
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
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
                        onClick = { viewModel!!.workOnGroup() }
                    )
                }
            }
            LogoPicker(
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
    private fun GroupDetails(
        descriptionModifier: Modifier = Modifier
    ) {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = viewModel!!.groupName,
            isError = viewModel!!.groupNameError,
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
            value = viewModel!!.groupDescription,
            isError = viewModel!!.groupDescriptionError,
            validator = { isGroupDescriptionValid(it) },
            label = Res.string.description,
            errorText = Res.string.wrong_description,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    private fun LogoPicker(
        modifier: Modifier = Modifier,
        iconSize: Dp
    ) {
        ImagePicker(
            modifier = modifier,
            iconSize = iconSize,
            imageData = viewModel!!.groupLogo.value,
            contentDescription = "Group Logo",
            onImagePicked = { logo ->
                viewModel!!.groupLogo.value = getImagePath(
                    imagePic = logo
                )
            }
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveGroup()
        viewModel!!.retrieveUserProjects()
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel!!.group.collectAsState()
        viewModel!!.groupNameError = remember { mutableStateOf(false) }
        viewModel!!.groupDescriptionError = remember { mutableStateOf(false) }
        viewModel!!.candidateMembersAvailable = remember {
            mutableStateOf(viewModel!!.retrieveCandidateMembers().isNotEmpty())
        }
    }

}