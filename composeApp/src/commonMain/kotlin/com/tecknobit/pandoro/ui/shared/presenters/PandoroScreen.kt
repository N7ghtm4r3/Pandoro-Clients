package com.tecknobit.pandoro.ui.shared.presenters

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The [PandoroScreen] class is useful to provides the basic behavior of a Pandoro's UI screen
 *
 * @param viewModel The support viewmodel for the screen
 *
 * @property V generic type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@Structure
@ScreenCoordinator
abstract class PandoroScreen<V : EquinoxViewModel>(
    private val onNavBack: (() -> Unit)? = null,
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        /**
         * `FORM_CARD_WIDTH` constant value for the width of a card used as form
         */
        val FORM_CARD_WIDTH = 750.dp

        /**
         * `FORM_CARD_HEIGHT` constant value for the height of a card used as form
         */
        val FORM_CARD_HEIGHT = 550.dp

    }

    @Composable
    override fun ArrangeScreenContent() {
        PandoroTheme {
            Column(
                /*modifier = Modifier
                    .padding(
                        paddingValues = paddingValues
                    )
                    .navigationBarsPadding(),*/
            ) {
                TitleSection()
                SubTitleSection()
                ScreenContent()
            }
        }
    }

    @Composable
    @ScreenSection
    @NonRestartableComposable
    protected open fun TitleSection() {
    }

    @Composable
    @ScreenSection
    @NonRestartableComposable
    protected open fun SubTitleSection() {

    }

    /**
     * The custom content of the screen
     */
    @Composable
    protected abstract fun ColumnScope.ScreenContent()

    /**
     * Method to dynamically place the content based on the current navigation mode
     *
     * @param paddingValues The values of the padding to apply to the [content]
     * @param screenTitle The title of the screen
     * @param subTitle The subtle of the screen
     * @param content The content of the screen
     */
    @Composable
    @NonRestartableComposable
    protected fun PlaceContent(
        paddingValues: PaddingValues = PaddingValues(
            top = 20.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 16.dp
        ),
        screenTitle: @Composable (() -> Unit)? = null,
        subTitle: @Composable (() -> Unit)? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(
                    paddingValues = paddingValues
                )
                .navigationBarsPadding(),
            content = {
                screenTitle?.invoke()
                subTitle?.invoke()
                content.invoke(this)
            }
        )
    }

    /**
     * Custom navigation-back button
     *
     * @param navBackAction The action to execute when user navigates back
     */
    @Composable
    @NonRestartableComposable
    protected fun NabBackButton(
        navBackAction: () -> Unit = { navigator.goBack() }
    ) {
        IconButton(
            modifier = Modifier
                .size(30.dp),
            onClick = navBackAction
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }

    /**
     * TitleSection of the screen
     *
     * @param modifier The modifier to apply to the title
     * @param navBackAction The action to execute when user navigates back
     * @param title The title string to set
     */
    @Composable
    @NonRestartableComposable
    protected fun ScreenTitle(
        modifier: Modifier = Modifier,
        navBackAction: (() -> Unit)? = null,
        title: StringResource
    ) {
        ScreenTitle(
            modifier = modifier,
            navBackAction = navBackAction,
            title = stringResource(title)
        )
    }

    /**
     * TitleSection of the screen
     *
     * @param modifier The modifier to apply to the title
     * @param navBackAction The action to execute when user navigates back
     * @param title The title string to set
     */
    @Composable
    @NonRestartableComposable
    protected fun ScreenTitle(
        modifier: Modifier = Modifier,
        navBackAction: (() -> Unit)? = null,
        title: String
    ) {
        if(navBackAction != null) {
            Row (
                modifier = modifier
                    .background(MaterialTheme.colorScheme.primary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                NabBackButton {
                    navBackAction.invoke()
                }
                TitleSection(
                    title = title
                )
            }
        } else {
            TitleSection(
                title = title
            )
        }
    }

    /**
     * TitleSection component
     *
     * @param modifier The modifier to apply to the component
     * @param title The title to set
     */
    @Composable
    @NonRestartableComposable
    protected fun TitleSection(
        modifier: Modifier = Modifier,
        title: String
    ) {
        Text(
            modifier = modifier,
            text = title,
            fontFamily = displayFontFamily,
            fontSize = 35.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }

    /**
     * Section organizer component
     *
     * @param modifier The modifier to apply to the component
     * @param header The header resource of the section
     * @param filtersContent The component to use to filters the data displayed by the section
     * @param content The content of the section
     */
    @Composable
    @ScreenSection
    protected fun Section(
        modifier: Modifier = Modifier,
        header: StringResource,
        filtersContent: @Composable () -> Unit = {},
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column (
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = stringResource(header),
                    fontFamily = displayFontFamily,
                    fontSize = 22.sp
                )
                filtersContent.invoke()
            }
            Column(
                content = content
            )
        }
    }

}