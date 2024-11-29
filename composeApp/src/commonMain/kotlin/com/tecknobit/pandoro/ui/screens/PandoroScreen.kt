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

        private val bottomNavigationMode = 16.dp

        private val sideNavigationMode = 141.dp

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
                    start = calculatedPaddingStart()
                ),
            content = content
        )
    }

    @Composable
    fun calculatedPaddingStart(): Dp {
        return if(isBottomNavigationMode.value)
            bottomNavigationMode
        else
            sideNavigationMode
    }


}