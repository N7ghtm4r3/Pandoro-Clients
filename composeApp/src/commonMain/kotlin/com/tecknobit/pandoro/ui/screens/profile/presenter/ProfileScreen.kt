package com.tecknobit.pandoro.ui.screens.profile.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Compact
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.ui.components.DeleteAccount
import com.tecknobit.pandoro.ui.components.Logout
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.theme.Green
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.change_email
import pandoro.composeapp.generated.resources.change_language
import pandoro.composeapp.generated.resources.change_password
import pandoro.composeapp.generated.resources.change_theme
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.logout
import pandoro.composeapp.generated.resources.new_email
import pandoro.composeapp.generated.resources.new_password
import pandoro.composeapp.generated.resources.profile
import pandoro.composeapp.generated.resources.settings
import pandoro.composeapp.generated.resources.wrong_email
import pandoro.composeapp.generated.resources.wrong_password

class ProfileScreen : PandoroScreen<ProfileScreenViewModel>(
    viewModel = ProfileScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold (
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = Res.string.profile
                    ) {
                        ProfileContent()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ProfileContent() {
        val widthSizeClass = getCurrentWidthSizeClass()
        when(widthSizeClass) {
            Compact -> { CompactProfile() }
            else -> {

            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun CompactProfile() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UserDetails()
            Settings()
        }
    }

    @Composable
    @NonRestartableComposable
    private fun UserDetails() {
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            ProfilePicker()
            Column (
                modifier = Modifier
                    .padding(
                        top = 10.dp
                    )
            ) {
                Text(
                    text = localUser.completeName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = localUser.email,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp
                )
                ActionButtons()
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ActionButtons() {
        Row (
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val logout = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.inversePrimary
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { logout.value = true }
            ) {
                ChameleonText(
                    text = stringResource(Res.string.logout),
                    fontSize = 12.sp,
                    backgroundColor = MaterialTheme.colorScheme.inversePrimary
                )
            }
            Logout(
                viewModel = viewModel!!,
                show = logout
            )
            val deleteAccount = remember { mutableStateOf(false) }
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(
                    size = 10.dp
                ),
                onClick = { deleteAccount.value = true }
            ) {
                Text(
                    text = stringResource(Res.string.delete),
                    fontSize = 12.sp
                )
            }
            DeleteAccount(
                viewModel = viewModel!!,
                show = deleteAccount
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProfilePicker() {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single
        ) { imagePath ->
            val newProfilePic = getImagePath(
                imagePic = imagePath
            )
            newProfilePic?.let {
                viewModel!!.changeProfilePic(
                    imagePath = newProfilePic,
                    profilePic = viewModel!!.profilePic
                )
            }
        }
        Thumbnail(
            size = 100.dp,
            contentDescription = "Profile pic",
            thumbnailData = viewModel!!.profilePic.value,
            onClick = { launcher.launch() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun Settings() {
        Section(
            header = Res.string.settings,
            content = {
                ProfileAction(
                    shape = RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    ),
                    leadingIcon = Icons.Default.AlternateEmail,
                    actionText = Res.string.change_email,
                    actionContent = { ChangeEmail() },
                    action = { visible ->
                        changeEmail {
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    leadingIcon = Icons.Default.Password,
                    actionText = Res.string.change_password,
                    actionContent = { ChangePassword() },
                    action = { visible ->
                        changePassword {
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    leadingIcon = Icons.Default.Language,
                    actionText = Res.string.change_language,
                    actionContent = {

                    },
                    action = { visible ->
                        changeEmail {
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    shape = RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    leadingIcon = Icons.Default.Palette,
                    actionText = Res.string.change_theme,
                    actionContent = {

                    },
                    action = { visible ->
                    },
                    bottomDivider = false
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ChangeEmail() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.newEmail = remember { mutableStateOf("") }
        viewModel!!.newEmailError = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider()
            EquinoxTextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                textFieldColors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                value = viewModel!!.newEmail,
                textFieldStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = bodyFontFamily
                ),
                isError = viewModel!!.newEmailError,
                mustBeInLowerCase = true,
                allowsBlankSpaces = false,
                validator = { isEmailValid(it) },
                errorText = Res.string.wrong_email,
                errorTextStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = bodyFontFamily
                ),
                placeholder = Res.string.new_email,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Email
                )
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ChangePassword() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.newPassword = remember { mutableStateOf("") }
        viewModel!!.newPasswordError = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider()
            EquinoxTextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                textFieldColors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                value = viewModel!!.newPassword,
                textFieldStyle = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = bodyFontFamily
                ),
                isError = viewModel!!.newPasswordError,
                allowsBlankSpaces = false,
                validator = { isPasswordValid(it) },
                errorText = Res.string.wrong_password,
                errorTextStyle = TextStyle(
                    fontSize = 14.sp,
                    fontFamily = bodyFontFamily
                ),
                placeholder = Res.string.new_password,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Section(
        header: StringResource,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column (
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = stringResource(header),
                fontFamily = displayFontFamily,
                fontSize = 22.sp
            )
            Column(
                content = content
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProfileAction(
        shape: Shape = RoundedCornerShape(
            size = 0.dp
        ),
        leadingIcon: ImageVector,
        actionText: StringResource,
        actionContent: @Composable () -> Unit,
        action: ProfileScreenViewModel.(MutableState<Boolean>) -> Unit,
        bottomDivider: Boolean = true
    ) {
        Card (
            shape = shape
        ) {
            val expanded = remember { mutableStateOf(false) }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier
                        .padding(
                            start = 10.dp
                        ),
                    text = stringResource(actionText)
                )
                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.End
                ) {
                    AnimatedVisibility(
                        visible = expanded.value
                    ) {
                        Row{
                            IconButton(
                                onClick = { expanded.value = !expanded.value }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Cancel,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            IconButton(
                                onClick = {
                                    action.invoke(
                                        viewModel!!,
                                        expanded
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Green()
                                )
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = !expanded.value
                    ) {
                        IconButton(
                            onClick = { expanded.value = true }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
            AnimatedVisibility(
                visible = expanded.value
            ) {
                actionContent.invoke()
            }
        }
        if(bottomDivider)
            HorizontalDivider()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.profilePic = remember { mutableStateOf(localUser.profilePic) }
    }

}