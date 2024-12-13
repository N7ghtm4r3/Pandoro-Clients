package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class ItemScreen<I, V: EquinoxViewModel>(
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    protected lateinit var item: State<I?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        LoadAwareContent()
    }

    @Composable
    @NonRestartableComposable
    // FIXME: (DEPRECATED TO TRIGGER SEARCH) REPLACE WITH THE REAL COMPONENT
    private fun LoadAwareContent() {
        PandoroTheme {
            AnimatedVisibility(
                visible = item.value != null,
                enter = fadeIn(),
                exit = fadeOut(),
                content = { ItemLoadedContent() }
            )
            AnimatedVisibility(
                visible = item.value == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface {
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(85.dp),
                            strokeWidth = 8.dp
                        )
                        Text(
                            modifier = Modifier
                                .padding(
                                    top = 16.dp
                                ),
                            text = stringResource(com.tecknobit.equinoxcompose.resources.Res.string.loading_data)
                        )
                    }
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    protected fun ItemLoadedContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    floatingActionButton = { FabAction() }
                ) {
                    PlaceContent(
                        paddingValues = PaddingValues(
                            top = 16.dp,
                            start = 6.dp,
                            end = 16.dp,
                            bottom = 16.dp
                        ),
                        screenTitle = { ItemScreenTitle() },
                    ) {
                        ScreenContent()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemScreenTitle()

    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.ScreenContent()

    @Composable
    @NonRestartableComposable
    protected abstract fun FabAction()

}