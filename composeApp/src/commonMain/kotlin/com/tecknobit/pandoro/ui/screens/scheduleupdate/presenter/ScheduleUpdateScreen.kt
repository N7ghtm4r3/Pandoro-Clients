package com.tecknobit.pandoro.ui.screens.scheduleupdate.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.ui.icons.AddNotes
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.scheduleupdate.presentation.ScheduleUpdateScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.screens.CreateScreen
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
                    ),
                shape = RoundedCornerShape(
                    size = 30.dp
                )
            ) {
                Column {
                    TargetVersion()
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
                .fillMaxSize(),
            shape = RoundedCornerShape(
                size = 30.dp
            )
        ) {
            Column {
                TargetVersion()
                ChangeNoteForm()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun TargetVersion() {
        EquinoxTextField(
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
    private fun ChangeNoteForm() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            EquinoxOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = remember { mutableStateOf("") },
                placeholder = Res.string.content_of_the_note,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(
                        onClick = {

                        }
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
    }

}