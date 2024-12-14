@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.pandoro.ui.screens.notes.data.Note
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.NotesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.content_of_the_note

@Composable
@NonRestartableComposable
fun NoteForm(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: NotesManager,
    update: ProjectUpdate? = null,
    note: Note? = null,
    onDismiss: (() -> Unit)? = null
) {
    if(state.isVisible) {
       /* viewModel.content = remember {
            mutableStateOf(
                if(note != null)
                    note.content
                else
                    ""
            )
        }*/
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    onDismiss?.invoke()
                    state.hide()
                }
            }
        ) {
            val focusRequester = remember { FocusRequester() }
            val keyboardController = LocalSoftwareKeyboardController.current
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Box (
                    modifier = Modifier
                        .imePadding()
                ) {
                    EquinoxTextField(
                        modifier = Modifier
                            .focusRequester(
                                focusRequester = focusRequester
                            )
                            .fillMaxWidth(),
                        textFieldColors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent
                        ),
                        value = remember { mutableStateOf("") },
                        placeholder = Res.string.content_of_the_note,
                        maxLines = Int.MAX_VALUE,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                                scope.launch {
                                    state.expand()
                                }
                            }
                        ),
                    )
                    SmallFloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(
                                all = 16.dp
                            ),
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}