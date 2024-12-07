package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.helpers.session.ManagedContent
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.bodyFontFamily
import com.tecknobit.pandoro.ui.screens.PandoroScreen
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.retry_to_reconnect

@Structure
abstract class ListsScreen<V: MultipleListViewModel>(
    viewModel: V,
    private val screenTitle: StringResource
): PandoroScreen<V> (
    viewModel = viewModel
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ManagedContent(
            viewModel = viewModel!!,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) },
                    floatingActionButton = { FabAction() },
                    bottomBar = { AdaptBottomBarToNavigationMode() }
                ) {
                    AdaptContentToNavigationMode(
                        screenTitle = screenTitle
                    ) {
                        ItemsInRow()
                        ItemsAdaptedSize()
                    }
                }
            },
            serverOfflineRetryText = stringResource(Res.string.retry_to_reconnect),
            serverOfflineRetryAction = { viewModel!!.retryRetrieveLists() }
        )
    }

    @Composable
    @NonRestartableComposable
    abstract fun FabAction()

    @Composable
    @NonRestartableComposable
    abstract fun ItemsInRow()

    @Composable
    @NonRestartableComposable
    fun EmptyResultWithFilters(
        info: StringResource
    ) {
        Text(
            modifier = Modifier
                .padding(
                    vertical = 10.dp
                ),
            text = stringResource(info),
            fontSize = 14.sp
        )
    }

    @Composable
    @NonRestartableComposable
    abstract fun ItemsAdaptedSize()

    @Composable
    @NonRestartableComposable
    protected fun NoDataAvailable(
        icon: ImageVector,
        subText: StringResource
    ) {
        EmptyListUI(
            icon = icon,
            subText = subText,
            textStyle = TextStyle(
                fontFamily = bodyFontFamily
            ),
            themeColor = MaterialTheme.colorScheme.inversePrimary
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun SectionHeader(
        header: StringResource,
        isAllItemsFiltering: Boolean
    ) {
        val filter = remember { mutableStateOf(false) }
        val areFiltersSet = viewModel!!.areFiltersSet(
            allItems = isAllItemsFiltering
        )
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(header)
            )
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {
                    if(areFiltersSet) {
                        viewModel!!.clearFilters(
                            allItems = isAllItemsFiltering
                        )
                    } else
                        filter.value = true
                }
            ) {
                Icon(
                    imageVector = if(areFiltersSet)
                        Icons.Default.FilterListOff
                    else
                        Icons.Default.FilterList,
                    contentDescription = null
                )
            }
        }
        if(isAllItemsFiltering) {
            FilterAllItemsUi(
                show = filter
            )
        } else {
            FilterRowItemsUi(
                show = filter
            )
        }
    }

    @Composable
    @NonRestartableComposable
    abstract fun FilterRowItemsUi(
        show: MutableState<Boolean>
    )

    @Composable
    @NonRestartableComposable
    abstract fun FilterAllItemsUi(
        show: MutableState<Boolean>
    )

}