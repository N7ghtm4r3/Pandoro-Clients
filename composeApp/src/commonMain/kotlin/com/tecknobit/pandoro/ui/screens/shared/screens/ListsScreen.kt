@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.pandoro.ui.screens.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowContainer
import com.tecknobit.equinoxcompose.session.sessionflow.rememberSessionFlowState
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.pandoro.ui.screens.home.presenter.HomeScreen
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.MultipleListViewModel
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.retry_to_reconnect

/**
 * The [ListsScreen] serves as a base screen to display multiple lists in one screen, one horizontal
 * other one vertical or grid format
 *
 * @param viewModel The support viewmodel of the screen
 * @param screenTitle The title of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
 * @see PandoroScreen
 */
@Structure
abstract class ListsScreen<V: MultipleListViewModel>(
    viewModel: V,
    private val screenTitle: StringResource
): PandoroScreen<V>(
    viewModel = viewModel
) {

    @Composable
    override fun ColumnScope.ScreenContent() {
        SessionFlowContainer(
            state = viewModel.sessionFlowState,
            content = {
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.primary,
                    snackbarHost = { SnackbarHost(viewModel.snackbarHostState!!) },
                    floatingActionButton = { FabAction() }
                ) {
                    Column {
                        ItemsInRow()
                        ItemsAdaptedSize()
                    }
                }
            },
            onServerOffline = {
                TextButton(
                    onClick = {
                        viewModel.retryRetrieveLists()
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.retry_to_reconnect)
                    )
                }
            }
        )
    }

    @Composable
    override fun TitleSection() {
        ScreenTitle(
            title = screenTitle
        )
    }

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    abstract fun FabAction()

    /**
     * The horizontal list to display the items in row format
     */
    @Composable
    abstract fun ItemsInRow()

    /**
     * The UI to display when, after filtering, the set of the data is empty
     *
     * @param info The info text to warn about the result of the filtering action
     */
    @Composable
    protected fun EmptyResultWithFilters(
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

    /**
     * The column or grid list dynamically adapted based on the screen size
     */
    @Composable
    abstract fun ItemsAdaptedSize()

    /**
     * The header of a section
     *
     * @param header The header value
     * @param isAllItemsFiltering Whether the filter is about the [ItemsAdaptedSize] list
     */
    @Composable
    protected fun SectionHeader(
        header: StringResource,
        isAllItemsFiltering: Boolean
    ) {
        val filter = remember { mutableStateOf(false) }
        val areFiltersSet = viewModel.areFiltersSet(
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
                        viewModel.clearFilters(
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

    /**
     * UI to filter the [ItemsInRow] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    abstract fun FilterRowItemsUi(
        show: MutableState<Boolean>
    )

    /**
     * UI to filter the [ItemsAdaptedSize] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    abstract fun FilterAllItemsUi(
        show: MutableState<Boolean>
    )

    /**
     * Method invoked when the [ShowContent] composable has been created.
     *
     * If the [viewModel] of the screen is not `null` will be set the [com.tecknobit.equinox.FetcherManager.activeContext]
     * as the current screen displayed
     *
     */
    override fun onCreate() {
        viewModel.setActiveContext(HomeScreen::class)
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        viewModel.sessionFlowState = rememberSessionFlowState()
    }

}