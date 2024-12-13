package com.tecknobit.pandoro.ui.screens.profile.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsPaused
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxbackend.environment.models.EquinoxUser.ApplicationTheme
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.LANGUAGES_SUPPORTED
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isEmailValid
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.isPasswordValid
import com.tecknobit.pandoro.SPLASHSCREEN
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.getCurrentWidthSizeClass
import com.tecknobit.pandoro.getImagePath
import com.tecknobit.pandoro.localUser
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteAccount
import com.tecknobit.pandoro.ui.components.Logout
import com.tecknobit.pandoro.ui.components.Thumbnail
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.profile.components.ChangelogItem
import com.tecknobit.pandoro.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.pandoro.ui.theme.Green
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
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
import pandoro.composeapp.generated.resources.changelogs
import pandoro.composeapp.generated.resources.delete
import pandoro.composeapp.generated.resources.logout
import pandoro.composeapp.generated.resources.new_email
import pandoro.composeapp.generated.resources.new_password
import pandoro.composeapp.generated.resources.no_changelogs_available
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
                        ProfileContentManager()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ProfileContentManager() {
        val widthSizeClass = getCurrentWidthSizeClass()
        when(widthSizeClass) {
            Expanded -> {
                ProfileContent(
                    modifier = Modifier
                        .widthIn(
                            max = FORM_CARD_WIDTH
                        )
                )
            }
            else -> { ProfileContent() }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ProfileContent(
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            UserDetails()
            Settings()
            Changelogs()
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
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ),
            size = 100.dp,
            contentDescription = "Profile pic",
            thumbnailData = viewModel!!.profilePic.value,
            onClick = { launcher.launch() }
        )
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
                    confirmAction = { visible ->
                        changeEmail {
                            visible.value = false
                        }
                    }
                )
                ProfileAction(
                    leadingIcon = Icons.Default.Password,
                    actionText = Res.string.change_password,
                    actionContent = { ChangePassword() },
                    confirmAction = { visible ->
                        changePassword {
                            visible.value = false
                        }
                    }
                )
                val language = remember { mutableStateOf(localUser.language) }
                ProfileAction(
                    leadingIcon = Icons.Default.Language,
                    actionText = Res.string.change_language,
                    actionContent = {
                        ChangeLanguage(
                            currentLanguage = language
                        )
                    },
                    dismissAction = { language.value = localUser.language },
                    confirmAction = { visible ->
                        changeLanguage(
                            newLanguage = language.value,
                            onSuccess = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
                    }
                )
                val theme = remember { mutableStateOf(localUser.theme) }
                ProfileAction(
                    shape = RoundedCornerShape(
                        bottomStart = 12.dp,
                        bottomEnd = 12.dp
                    ),
                    leadingIcon = Icons.Default.Palette,
                    actionText = Res.string.change_theme,
                    actionContent = {
                        ChangeTheme(
                            currentTheme = theme
                        )
                    },
                    dismissAction = { theme.value = localUser.theme },
                    confirmAction = { visible ->
                        changeTheme(
                            newTheme = theme.value,
                            onChange = {
                                visible.value = false
                                navigator.navigate(SPLASHSCREEN)
                            }
                        )
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

    @Composable
    @NonRestartableComposable
    private fun ChangePassword() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        viewModel!!.newPassword = remember { mutableStateOf("") }
        viewModel!!.newPasswordError = remember { mutableStateOf(false) }
        var hiddenPassword by remember { mutableStateOf(true) }
        EquinoxOutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester),
            outlinedTextFieldColors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            value = viewModel!!.newPassword,
            outlinedTextFieldStyle = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = bodyFontFamily
            ),
            isError = viewModel!!.newPasswordError,
            allowsBlankSpaces = false,
            trailingIcon = {
                IconButton(
                    onClick = { hiddenPassword = !hiddenPassword }
                ) {
                    Icon(
                        imageVector = if (hiddenPassword)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            },
            visualTransformation = if (hiddenPassword)
                PasswordVisualTransformation()
            else
                VisualTransformation.None,
            validator = { isPasswordValid(it) },
            errorText = stringResource(Res.string.wrong_password),
            errorTextStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = bodyFontFamily
            ),
            placeholder = stringResource(Res.string.new_password),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            )
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ChangeLanguage(
        currentLanguage: MutableState<String>
    ) {
        Column (
            modifier = Modifier
                .selectableGroup()
        ) {
            LANGUAGES_SUPPORTED.entries.forEach { entry ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage.value == entry.key,
                        onClick = { currentLanguage.value = entry.key }
                    )
                    Text(
                        text = entry.value
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun ChangeTheme(
        currentTheme: MutableState<ApplicationTheme>
    ) {
        Column (
            modifier = Modifier
                .selectableGroup()
        ) {
            ApplicationTheme.entries.forEach { entry ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentTheme.value == entry,
                        onClick = { currentTheme.value = entry }
                    )
                    Text(
                        text = entry.name
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Changelogs() {
        Section(
            header = Res.string.changelogs,
            content = {
                ProfileAction(
                    shape = RoundedCornerShape(
                        size = 12.dp
                    ),
                    leadingIcon = Icons.Default.Notifications,
                    actionText = Res.string.changelogs,
                    actionContent = {
                        PaginatedLazyColumn(
                            modifier = Modifier
                                .heightIn(
                                    max = FORM_CARD_HEIGHT
                                )
                                .animateContentSize(),
                            paginationState = viewModel!!.changelogsState,
                            firstPageEmptyIndicator = {
                                EmptyListUI(
                                    containerModifier = Modifier
                                        .padding(
                                            bottom = 10.dp
                                        ),
                                    icon = Icons.Default.NotificationsPaused,
                                    subText = Res.string.no_changelogs_available,
                                    textStyle = TextStyle(
                                        fontFamily = bodyFontFamily
                                    )
                                )
                            }
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
                            items(
                                items = viewModel!!.changelogsState.allItems!!,
                                key = { changelog -> changelog.id }
                            ) { changelog ->
                                ChangelogItem(
                                    viewModel = viewModel!!,
                                    changelog = changelog
                                )
                            }
                        }
                    },
                    controls = { expanded ->
                        Column (
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            IconButton(

                                onClick = { expanded.value = !expanded.value }
                            ) {
                                Icon(
                                    imageVector = if(expanded.value)
                                        Icons.Default.KeyboardArrowUp
                                    else
                                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    bottomDivider = false
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ProfileAction(
        shape: Shape = RoundedCornerShape(
            size = 0.dp
        ),
        leadingIcon: ImageVector,
        actionText: StringResource,
        actionContent: @Composable ColumnScope.() -> Unit,
        dismissAction: (() -> Unit)? = null,
        confirmAction: ProfileScreenViewModel.(MutableState<Boolean>) -> Unit,
        bottomDivider: Boolean = true
    ) {
        ProfileAction(
            shape = shape,
            leadingIcon = leadingIcon,
            actionText = actionText,
            actionContent = actionContent,
            controls = { expanded ->
                ActionControls(
                    expanded = expanded,
                    dismissAction = dismissAction,
                    confirmAction = confirmAction
                )
            },
            bottomDivider = bottomDivider,
        )
    }

    @Composable
    @NonRestartableComposable
    private fun ActionControls(
        expanded: MutableState<Boolean>,
        dismissAction: (() -> Unit)?,
        confirmAction: ProfileScreenViewModel.(MutableState<Boolean>) -> Unit,
    ) {
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
                        onClick = {
                            dismissAction?.invoke()
                            expanded.value = !expanded.value
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    IconButton(
                        onClick = {
                            confirmAction.invoke(
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

    @Composable
    @NonRestartableComposable
    private fun ProfileAction(
        shape: Shape = RoundedCornerShape(
            size = 0.dp
        ),
        leadingIcon: ImageVector,
        actionText: StringResource,
        actionContent: @Composable ColumnScope.() -> Unit,
        controls: @Composable (MutableState<Boolean>) -> Unit,
        bottomDivider: Boolean = true
    ) {
        Card (
            shape = shape
        ) {
            val expanded = rememberSaveable { mutableStateOf(false) }
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
                controls.invoke(expanded)
            }
            AnimatedVisibility(
                visible = expanded.value
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    HorizontalDivider()
                    actionContent.invoke(this)
                }
            }
        }
        if(bottomDivider)
            HorizontalDivider()
    }

    override fun onCreate() {
        viewModel!!.setActiveContext(HomeScreen::class.java)
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        viewModel!!.profilePic = remember { mutableStateOf(localUser.profilePic) }
    }

}