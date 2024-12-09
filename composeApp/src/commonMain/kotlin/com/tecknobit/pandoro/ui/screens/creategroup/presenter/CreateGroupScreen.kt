@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.creategroup.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.creategroup.presentation.CreateGroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.groups.data.Group.Companion.asText
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import com.tecknobit.pandoro.ui.theme.Green
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupDescriptionValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isGroupNameValid
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.add_projects
import pandoro.composeapp.generated.resources.create_group
import pandoro.composeapp.generated.resources.description
import pandoro.composeapp.generated.resources.edit_group
import pandoro.composeapp.generated.resources.invite
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.remove
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
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if(viewModel!!.candidateMembersAvailable.value) {
                    val membersSheetState = rememberModalBottomSheetState()
                    val membersScope = rememberCoroutineScope()
                    FloatingActionButton(
                        onClick = {
                            membersScope.launch {
                                membersSheetState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null
                        )
                    }
                    ManageGroupMembers(
                        modalBottomSheetState = membersSheetState,
                        scope = membersScope
                    )
                }
                if(viewModel!!.userProjects.isNotEmpty()) {
                    val projectsSheetState = rememberModalBottomSheetState()
                    val projectsScope = rememberCoroutineScope()
                    FloatingActionButton(
                        onClick = {
                            projectsScope.launch {
                                projectsSheetState.show()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreateNewFolder,
                            contentDescription = null
                        )
                    }
                    GroupProjects(
                        modalBottomSheetState = projectsSheetState,
                        scope = projectsScope
                    )
                }
            }
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
                                    .weight(1.3f)
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
    private fun ManageGroupMembers(
        modalBottomSheetState: SheetState,
        scope: CoroutineScope
    ) {
        if(modalBottomSheetState.isVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        modalBottomSheetState.hide()
                    }
                }
            ) {
                GroupMembers()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupMembers(
        modifier: Modifier = Modifier
    ) {
        PaginatedLazyColumn(
            modifier = modifier
                .animateContentSize(),
            paginationState = viewModel!!.candidateMembersState,
            contentPadding = PaddingValues(
                vertical = 10.dp
            ),
            firstPageEmptyIndicator = { viewModel!!.candidateMembersAvailable.value = false }
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
            viewModel!!.candidateMembersAvailable.value = true
            items(
                items = viewModel!!.candidateMembersState.allItems!!,
                key = { member -> member.id }
            ) { member ->
                GroupMember(
                    member = member
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun GroupMember(
        member: GroupMember
    ) {
        ListItem(
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            leadingContent = {
                Thumbnail(
                    size = 50.dp,
                    thumbnailData = member.profilePic,
                    contentDescription = "Member profile pic"
                )
            },
            overlineContent = {
                val isAdmin = member.isAnAdmin()
                Text(
                    text = member.role.asText(),
                    color = if(isAdmin)
                        MaterialTheme.colorScheme.error
                    else
                        Color.Unspecified
                )
            },
            headlineContent = {
                Text(
                    text = member.completeName()
                )
            },
            supportingContent = {
                Text(
                    text = member.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {
                if(viewModel!!.groupMembers.contains(member)) {
                    Button(
                        modifier = Modifier
                            .height(35.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        shape = RoundedCornerShape(
                            size = 5.dp
                        ),
                        onClick = { viewModel!!.groupMembers.remove(member) }
                    ) {
                        Text(
                            text = stringResource(Res.string.remove),
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Button(
                        modifier = Modifier
                            .height(35.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green()
                        ),
                        shape = RoundedCornerShape(
                            size = 5.dp
                        ),
                        onClick = { viewModel!!.groupMembers.add(member) }
                    ) {
                        Text(
                            text = stringResource(Res.string.invite),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(
                    start = 65.dp
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