package com.tecknobit.pandoro.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.ui.screens.home.HomeScreen.Companion.isBottomNavigationMode

@Structure
abstract class PandoroScreen<V : EquinoxViewModel>(
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        private val startPaddingBottomNavigationMode = 16.dp

        private val startPaddingSideNavigationMode = 141.dp

        private val bottomPaddingBottomNavigationMode = 80.dp

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
        content: @Composable ColumnScope.() -> Unit
    ) {
        Column(
            modifier = Modifier
                .padding(
                    top = 16.dp,
                    start = calculatedStartPadding(),
                    end = 16.dp,
                    bottom = calculatedBottomPadding()
                ),
            content = content
        )
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