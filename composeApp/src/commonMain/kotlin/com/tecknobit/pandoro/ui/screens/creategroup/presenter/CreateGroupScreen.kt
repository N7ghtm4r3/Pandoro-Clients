@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.creategroup.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.UIAnimations
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.creategroup.presentation.CreateGroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
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
import pandoro.composeapp.generated.resources.member_details_hint
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.no_members_in_group
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
                        GroupFormDetails(
                            modifier = Modifier
                                .weight(1f),
                        )
                        GroupMembers(
                            modifier = Modifier
                                .weight(1f)
                        )
                    }
                    GroupCardFormActions()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupFormDetails(
        modifier: Modifier
    ) {
        Column (
            modifier = modifier
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel!!.groupName,
                isError = viewModel!!.groupNameError,
                validator = { isGroupNameValid(it) },
                label = Res.string.name,
                errorText = Res.string.wrong_name
            )
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                maxLines = Int.MAX_VALUE,
                value = viewModel!!.groupDescription,
                isError = viewModel!!.groupDescriptionError,
                validator = { isGroupDescriptionValid(it) },
                label = Res.string.description,
                errorText = Res.string.wrong_description
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupMembers(
        modifier: Modifier
    ) {
        Column (
            modifier = modifier
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel!!.memberDetails,
                label = "",
                onValueChange = {
                    viewModel!!.memberDetails.value = it
                    viewModel!!.getCandidateMembers()
                },
                placeholder = stringResource(Res.string.member_details_hint)
            )
            DropdownMenu(
                expanded = viewModel!!.candidateMembers.isNotEmpty(),
                onDismissRequest = { }
            ) {
                viewModel!!.candidateMembers.forEach { member ->
                    DropdownMenuItem(
                        text = {
                            GroupMember(
                                member = member
                            )
                        },
                        onClick = {
                            viewModel!!.addCandidateMember(
                                member = member
                            )
                        }
                    )
                }
            }
            GroupMembers()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupMembers() {
        val groupHasMembers = viewModel!!.groupMembers.isNotEmpty()
        EmptyListUI(
            animations = UIAnimations(
                visible = !groupHasMembers,
                onEnter = fadeIn(),
                onExit = fadeOut()
            ),
            icon = Icons.Default.PersonOff,
            subText = stringResource(Res.string.no_members_in_group)
        )
        if(groupHasMembers) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize()
            ) {
                items(
                    items = viewModel!!.groupMembers,
                    key = { member -> member.id }
                ) { member ->
                    GroupMember(
                        member = member
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupMember(
        member: GroupMember
    ) {
        ListItem(
            leadingContent = {
                Thumbnail(
                    thumbnailData = member.profilePic,
                    contentDescription = "Member profile pic"
                )
            },
            headlineContent = {
                Text(
                    text = member.completeName()
                )
            },
            supportingContent = {
                Text(
                    text = member.email
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun GroupCardFormActions() {
        Row (
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
            ManageGroupProjects(
                modalBottomSheetState = modalBottomSheetState,
                scope = scope
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ManageGroupProjects(
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
                    /*IconPicker(
                        iconSize = 130.dp
                    )
                    ProjectCardFormDetails(
                        descriptionModifier = Modifier
                            .weight(1f)
                    )*/
                }
            }
        }
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
        viewModel!!.memberDetails = remember { mutableStateOf("") }
    }

}