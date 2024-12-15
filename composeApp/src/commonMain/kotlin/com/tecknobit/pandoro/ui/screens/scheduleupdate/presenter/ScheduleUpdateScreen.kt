package com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation.ScheduleUpdateScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isContentNoteValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.content_of_the_note
import pandoro.composeapp.generated.resources.edit
import pandoro.composeapp.generated.resources.schedule_update
import pandoro.composeapp.generated.resources.schedule_update_subtext
import pandoro.composeapp.generated.resources.target_version
import pandoro.composeapp.generated.resources.wrong_target_version

class ScheduleUpdateScreen(
    projectId: String,
    private val projectName: String
) : CreateScreen<ProjectUpdate, ScheduleUpdateScreenViewModel>(
    itemId = null,
    viewModel = ScheduleUpdateScreenViewModel(
        projectId = projectId
    )
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent(
            creationTitle = Res.string.schedule_update,
            editingTitle = Res.string.edit, //
            subTitle = {
                Text(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            bottom = 16.dp
                        ),
                    text = stringResource(
                        resource = Res.string.schedule_update_subtext,
                        projectName
                    ),
                    fontSize = 14.sp
                )
            }
        ) {

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
                .fillMaxSize()
                .padding(
                    all = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card (
                modifier = Modifier
                    .size(
                        width = FORM_CARD_WIDTH,
                        height = FORM_CARD_HEIGHT
                    )
            ) {
                Column {
                    TargetVersion()
                    ChangeNotes()
                    ChangeNoteForm()
                }
            }
        }
    }


    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun FullScreenForm() {
        super.FullScreenForm()
        Card (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column {
                TargetVersion()
                ChangeNotes()
                ChangeNoteForm()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun TargetVersion() {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .padding(
                    horizontal = 16.dp
                )
                .padding(
                    top = 16.dp
                )
                .fillMaxWidth(),
            value = viewModel!!.targetVersion,
            placeholder = Res.string.target_version,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            isError = viewModel!!.targetVersionError,
            errorText = Res.string.wrong_target_version,
            validator = { isValidVersion(it) }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ColumnScope.ChangeNotes() {
        LazyColumn (
            modifier = Modifier
                .weight(5f),
            contentPadding = PaddingValues(
                vertical = 16.dp
            )
        ) {
            items(
                items = viewModel!!.changeNotes
            ) { changeNote ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = changeNote,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                viewModel!!.removeChangeNote(
                                    changeNote = changeNote
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                HorizontalDivider()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ColumnScope.ChangeNoteForm() {
        Column(
            modifier = Modifier
                .weight(1f)
                .imePadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = viewModel!!.changeNoteContent,
                placeholder = Res.string.content_of_the_note,
                maxLines = 3,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    errorBorderColor = Color.Transparent
                ),
                isError = viewModel!!.changeNoteContentError,
                validator = { isContentNoteValid(it) },
                trailingIcon = {
                    IconButton(
                        onClick = { viewModel!!.addChangeNote() }
                    ) {
                        Icon(
                            imageVector = AddNotes,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel!!.targetVersion = remember { mutableStateOf("") }
        viewModel!!.targetVersionError = remember { mutableStateOf(false) }
        viewModel!!.changeNoteContent = remember { mutableStateOf("") }
        viewModel!!.changeNoteContentError = remember { mutableStateOf(false) }
    }

}