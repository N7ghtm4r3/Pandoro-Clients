package com.tecknobit.pandoro.ui.screens

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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen.Companion.isBottomNavigationMode
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class PandoroScreen<V : EquinoxViewModel>(
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        val FORM_CARD_WIDTH = 750.dp

        val FORM_CARD_HEIGHT = 550.dp

        private val startPaddingBottomNavigationMode = 16.dp

        private val startPaddingSideNavigationMode = 141.dp

        private val bottomPaddingBottomNavigationMode = 96.dp

        private val bottomPaddingSideNavigationMode = 16.dp

    }

    @Composable
    @NonRestartableComposable
    protected fun AdaptBottomBarToNavigationMode() {
        if(isBottomNavigationMode.value)
            BottomAppBar {  }
    }

    @Composable
    @NonRestartableComposable
    protected fun AdaptContentToNavigationMode(
        navBackAction: (() -> Unit)? = null,
        screenTitle: StringResource? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        PlaceContent(
            paddingValues = PaddingValues(
                top = 16.dp,
                start = calculatedStartPadding(),
                end = 16.dp,
                bottom = calculatedBottomPadding()
            ),
            navBackAction = navBackAction,
            screenTitle = screenTitle,
            content = content
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun PlaceContent(
        paddingValues: PaddingValues = PaddingValues(
            all = 16.dp
        ),
        titleModifier: Modifier = Modifier,
        navBackAction: (() -> Unit)? = null,
        screenTitle: StringResource? = null,
        subTitle: @Composable (() -> Unit)? = null,
        content: @Composable ColumnScope.() -> Unit
    ) {
        PlaceContent(
            paddingValues = paddingValues,
            titleModifier = titleModifier,
            navBackAction = navBackAction,
            screenTitle = if(screenTitle != null)
                stringResource(screenTitle)
            else
                null,
            subTitle = subTitle,
            content = content
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun PlaceContent(
        paddingValues: PaddingValues = PaddingValues(
            all = 16.dp
        ),
        titleModifier: Modifier = Modifier,
        navBackAction: (() -> Unit)? = null,
        screenTitle: String?,
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
                screenTitle?.let { title ->
                    ScreenTitle(
                        titleModifier = titleModifier,
                        navBackAction = navBackAction,
                        title = title
                    )
                }
                subTitle?.invoke()
                content.invoke(this)
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun PlaceContent(
        paddingValues: PaddingValues = PaddingValues(
            all = 16.dp
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

    @Composable
    @NonRestartableComposable
    protected fun ScreenTitle(
        navBackAction: (() -> Unit)? = null,
        titleModifier: Modifier = Modifier,
        title: String
    ) {
        if(navBackAction != null) {
            Row (
                modifier = titleModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                NabBackButton {
                    navBackAction.invoke()
                }
                Title(
                    title = title
                )
            }
        } else {
            Title(
                title = title
            )
        }
    }

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
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun Title(
        modifier: Modifier = Modifier,
        title: String
    ) {
        Text(
            modifier = modifier,
            text = title,
            fontFamily = displayFontFamily,
            fontSize = 35.sp
        )
    }

    @Composable
    @NonRestartableComposable
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

    @Composable
    fun calculatedStartPadding(): Dp {
        return if(isBottomNavigationMode.value)
            startPaddingBottomNavigationMode
        else
            startPaddingSideNavigationMode
    }

    @Composable
    fun calculatedBottomPadding(): Dp {
        return if(isBottomNavigationMode.value)
            bottomPaddingBottomNavigationMode
        else
            bottomPaddingSideNavigationMode
    }

}