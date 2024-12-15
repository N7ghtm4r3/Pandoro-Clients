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
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.screens.createnote.presentation.CreateNoteScreenViewModel
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.add_change_note_for_update
import pandoro.composeapp.generated.resources.content_of_the_note
import pandoro.composeapp.generated.resources.create_note
import pandoro.composeapp.generated.resources.edit_change_note_of_update
import pandoro.composeapp.generated.resources.edit_note

class CreateNoteScreen(
    private val updateId: String? = null,
    private val targetVersion: String? = null,
    noteId: String?
) : CreateScreen<Note, CreateNoteScreenViewModel>(
    itemId = noteId,
    viewModel = CreateNoteScreenViewModel(
        updateId = updateId,
        noteId = noteId
    )
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent(
            creationTitle = Res.string.create_note,
            editingTitle = Res.string.edit_note,
            subTitle = if(updateId != null) {
                {
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
            } else
                null
        ) {
            viewModel!!.content = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.content
                    else
                        ""
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = { viewModel!!.saveNote() }
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null
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

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun FullScreenForm() {
        super.FullScreenForm()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            ContentInput()
        }
    }

    @Composable
    @NonRestartableComposable
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
            value = viewModel!!.content,
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
        viewModel!!.retrieveNote()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel!!.note.collectAsState()
    }

}