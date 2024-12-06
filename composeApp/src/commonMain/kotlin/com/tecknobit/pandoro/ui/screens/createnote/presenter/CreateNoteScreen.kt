package com.tecknobit.pandoro.ui.screens.createnote.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.CreateScreen
import com.tecknobit.pandoro.ui.screens.createnote.presentation.CreateNoteScreenViewModel
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.content_of_the_note
import pandoro.composeapp.generated.resources.create_note
import pandoro.composeapp.generated.resources.edit_note

class CreateNoteScreen(
    noteId: String?
) : CreateScreen<Note, CreateNoteScreenViewModel>(
    itemId = noteId,
    viewModel = CreateNoteScreenViewModel(
        noteId = noteId
    )
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent {
            viewModel!!.content = remember {
                mutableStateOf(
                    if(isEditing)
                        item.value!!.content
                    else
                        ""
                )
            }
            Scaffold(
                containerColor = MaterialTheme.colorScheme.primary,
                snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                floatingActionButton = { FabAction() }
            ) {
                PlaceContent(
                    paddingValues = PaddingValues(
                        all = 0.dp
                    ),
                    titleModifier = Modifier
                        .padding(
                            top = 16.dp,
                            start = 16.dp
                        ),
                    navBackAction = { navigator.goBack() },
                    screenTitle = if(isEditing)
                        Res.string.edit_note
                    else
                        Res.string.create_note
                ) {
                    Form()
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        AnimatedVisibility(
            visible = fullScreenFormType.value,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            FloatingActionButton(
                onClick = { viewModel!!.saveNote() }
            ) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = null
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun CardForm() {
        super.CardForm()
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    override fun FullScreenForm() {
        super.FullScreenForm()
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {
            EquinoxTextField(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 10.dp,
                            topEnd = 10.dp
                        )
                    )
                    .focusRequester(
                        focusRequester = focusRequester
                    )
                    .fillMaxSize(),
                textFieldColors = TextFieldDefaults.colors(
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                value = viewModel!!.content,
                label = "",
                placeholder = stringResource(Res.string.content_of_the_note),
                maxLines = Int.MAX_VALUE,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                )
            )
        }
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