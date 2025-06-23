@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.createnote.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.screens.createnote.presentation.CreateNoteScreenViewModel
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.add_change_note_for_update
import pandoro.composeapp.generated.resources.content_of_the_note
import pandoro.composeapp.generated.resources.create_note
import pandoro.composeapp.generated.resources.edit_change_note_of_update
import pandoro.composeapp.generated.resources.edit_note

/**
 * The [CreateNoteScreen] display the form to create a new note or edit an existing one
 *
 * @param projectId The identifier of the project owns the update
 * @param updateId The identifier of the update owns the note
 * @param targetVersion The target version of the [updateId]
 * @param noteId The identifier of the note to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 * @see CreateScreen
 */
class CreateNoteScreen(
    projectId: String? = null,
    private val updateId: String? = null,
    private val targetVersion: String? = null,
    noteId: String?
) : CreateScreen<Note, CreateNoteScreenViewModel>(
    itemId = noteId,
    creationTitle = Res.string.create_note,
    editingTitle = Res.string.edit_note,
    viewModel = CreateNoteScreenViewModel(
        projectId = projectId,
        updateId = updateId,
        noteId = noteId
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
        if(updateId != null) {
            Text(
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        bottom = 16.dp
                    ),
                text = stringResource(
                    resource = if(isEditing)
                        Res.string.edit_change_note_of_update
                    else
                        Res.string.add_change_note_for_update,
                    targetVersion!!.asVersionText()
                ),
                fontSize = 14.sp
            )
        }
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = { viewModel.saveNote() }
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null
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
                ContentInput()
            }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp
                )
                .imePadding()
        ) {
            ContentInput()
        }
    }

    /**
     * [EquinoxTextField] used to insert the content of the note to save
     */
    @Composable
    private fun ContentInput() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        EquinoxTextField(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                )
                .focusRequester(
                    focusRequester = focusRequester
                )
                .fillMaxSize(),
            textFieldColors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent
            ),
            value = viewModel.content,
            placeholder = Res.string.content_of_the_note,
            maxLines = Int.MAX_VALUE,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveNote()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel.note.collectAsState()
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

    @Composable
    override fun CollectStatesAfterLoading() {
        viewModel.content = remember {
            mutableStateOf(
                if(isEditing)
                    item.value!!.content
                else
                    ""
            )
        }
    }

}