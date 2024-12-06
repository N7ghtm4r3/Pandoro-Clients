package com.tecknobit.pandoro.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.helpers.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcompose.resources.loading_data
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.getCurrentSizeClass
import com.tecknobit.pandoro.ui.theme.PandoroTheme
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class CreateScreen<I, V : EquinoxViewModel>(
    itemId: String?,
    viewModel: V
) : PandoroScreen<V>(
    viewModel = viewModel
) {

    protected val isEditing: Boolean = itemId != null

    protected lateinit var item: State<I?>

    protected lateinit var fullScreenFormType: MutableState<Boolean>

    @Composable
    @NonRestartableComposable
    // FIXME: (DEPRECATED TO TRIGGER SEARCH) REPLACE WITH THE REAL COMPONENT
    protected fun LoadAwareContent(
        itemLoadedContent: @Composable () -> Unit
    ) {
        PandoroTheme {
            AnimatedVisibility(
                visible = (isEditing && item.value != null) || !isEditing,
                enter = fadeIn(),
                exit = fadeOut(),
                content = { itemLoadedContent.invoke() }
            )
            AnimatedVisibility(
                visible = isEditing && item.value == null,
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
    protected abstract fun FabAction()

    @Composable
    @NonRestartableComposable
    protected fun Form() {
        val windowSizeClass = getCurrentSizeClass()
        val widthClass = windowSizeClass.widthSizeClass
        val heightClass = windowSizeClass.heightSizeClass
        when {
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Expanded -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Medium -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Expanded && heightClass == WindowHeightSizeClass.Medium -> {
                CardForm()
            }
            widthClass == WindowWidthSizeClass.Medium && heightClass == WindowHeightSizeClass.Expanded -> {
                CardForm()
            }
            else -> FullScreenForm()
        }
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    protected open fun CardForm() {
        fullScreenFormType.value = false
    }

    @Composable
    @NonRestartableComposable
    @RequiresSuperCall
    protected open fun FullScreenForm() {
        fullScreenFormType.value = true
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        fullScreenFormType = remember { mutableStateOf(false) }
    }

}