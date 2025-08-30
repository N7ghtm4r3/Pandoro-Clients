@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.screens.notes.components.NoteDetails
import com.tecknobit.pandoro.ui.screens.shared.data.project.Update
import com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation.ScheduleUpdateScreenViewModel
import com.tecknobit.pandoro.ui.screens.create.CreateScreen
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isContentNoteValid
import com.tecknobit.pandorocore.helpers.PandoroInputsValidator.isValidVersion
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.change_note_count
import pandoro.composeapp.generated.resources.content_of_the_note
import pandoro.composeapp.generated.resources.edit
import pandoro.composeapp.generated.resources.schedule
import pandoro.composeapp.generated.resources.schedule_update
import pandoro.composeapp.generated.resources.schedule_update_subtext
import pandoro.composeapp.generated.resources.target_version
import pandoro.composeapp.generated.resources.wrong_target_version

/**
 * The [ScheduleUpdateScreen] class allows to schedule a new update
 *
 * @param projectId The identifier of the project where the update must be attached
 * @param projectName The name of the project
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 */
class ScheduleUpdateScreen(
    projectId: String,
    private val projectName: String
) : CreateScreen<Update, ScheduleUpdateScreenViewModel>(
    itemId = null,
    creationTitle = Res.string.schedule_update,
    editingTitle = Res.string.edit,
    viewModel = ScheduleUpdateScreenViewModel(
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
     * The section where is displayed the subtitle of the current screen
     */
    @Composable
    @ScreenSection
    override fun SubtitleSection() {
        Text(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 10.dp
                ),
            text = stringResource(
                resource = Res.string.schedule_update_subtext,
                projectName
            ),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    override fun FabAction() {
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
                .fillMaxSize()
                .padding(
                    all = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ScheduleForm(
                modifier = Modifier
                    .size(
                        width = FORM_CARD_WIDTH,
                        height = FORM_CARD_HEIGHT
                    )
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
        ScheduleForm(
            modifier = Modifier
                .fillMaxSize()
        )
    }

    /**
     * The form used to insert the details of the new update
     *
     * @param modifier The modifier to apply to the form
     */
    @Composable
    @NonRestartableComposable
    private fun ScheduleForm(
        modifier: Modifier
    ) {
        Card (
            modifier = modifier
        ) {
            Column {
                FormHeader()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                        .navigationBarsPadding()
                ) {
                    ChangeNotes()
                    ChangeNoteForm()
                }
            }
        }
    }

    /**
     * The custom header of the [ScheduleForm]
     */
    @Composable
    @NonRestartableComposable
    private fun FormHeader() {
        Row(
            modifier = Modifier
                .padding(
                    top = 10.dp
                )
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EquinoxTextField(
                modifier = Modifier
                    .weight(1f),
                textFieldColors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = viewModel.targetVersion,
                placeholder = Res.string.target_version,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                isError = viewModel.targetVersionError,
                errorText = Res.string.wrong_target_version,
                validator = { isValidVersion(it) }
            )
            val localSoftInputKeyboard = LocalSoftwareKeyboardController.current
            Button(
                modifier = Modifier
                    .padding(
                        end = 16.dp
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    localSoftInputKeyboard?.hide()
                    viewModel.scheduleUpdate()
                }
            ) {
                Text(
                    text = stringResource(
                        Res.string.schedule
                    )
                )
            }
        }
    }

    /**
     * The list of the current change notes added to the related list
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeNotes() {
        LazyColumn (
            modifier = Modifier
                .animateContentSize(),
            contentPadding = PaddingValues(
                bottom = 55.dp
            )
        ) {
            itemsIndexed(
                items = viewModel.changeNotes
            ) { index, changeNote ->
                ChangeNoteItem(
                    index = index,
                    changeNote = changeNote
                )
            }
        }
    }

    /**
     * The change note added and its details
     *
     * @param index The index of the change note
     * @param changeNote The content of the change note
     */
    @Composable
    @NonRestartableComposable
    private fun ChangeNoteItem(
        index: Int,
        changeNote: String
    ) {
        val state = rememberModalBottomSheetState()
        val scope = rememberCoroutineScope()
        ListItem(
            modifier = Modifier
                .clickable {
                    scope.launch {
                        state.show()
                    }
                },
            overlineContent = {
                Text(
                    text = stringResource(
                        resource = Res.string.change_note_count,
                        index + 1
                    )
                )
            },
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
                        viewModel.removeChangeNote(
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
        NoteDetails(
            note = changeNote,
            state = state,
            scope = scope
        )
    }

    /**
     * The form to allow the user to type the content of the change note to add
     */
    @Composable
    @NonRestartableComposable
    private fun BoxScope.ChangeNoteForm() {
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            value = viewModel.changeNoteContent,
            placeholder = Res.string.content_of_the_note,
            maxLines = 3,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            outlinedTextFieldColors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                errorContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent
            ),
            isError = viewModel.changeNoteContentError,
            validator = { isContentNoteValid(it) },
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.addChangeNote() }
                ) {
                    Icon(
                        imageVector = AddNotes,
                        contentDescription = null
                    )
                }
            }
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel.targetVersion = remember { mutableStateOf("") }
        viewModel.targetVersionError = remember { mutableStateOf(false) }
        viewModel.changeNoteContent = remember { mutableStateOf("") }
        viewModel.changeNoteContentError = remember { mutableStateOf(false) }
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}