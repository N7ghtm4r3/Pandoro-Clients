package com.tecknobit.pandoro.ui.screens.groups.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.pandoro.CREATE_GROUP_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.FirstPageProgressIndicator
import com.tecknobit.pandoro.ui.components.NewHorizontalPageProgressIndicator
import com.tecknobit.pandoro.ui.screens.groups.components.FilterGroups
import com.tecknobit.pandoro.ui.screens.groups.components.Groups
import com.tecknobit.pandoro.ui.screens.groups.components.MyGroupCard
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import com.tecknobit.pandoro.ui.screens.shared.data.GroupMember.Companion.asText
import com.tecknobit.pandoro.ui.screens.shared.screens.ListsScreen
import com.tecknobit.pandoro.ui.shared.presenters.PandoroScreen
import com.tecknobit.pandorocore.enums.Role
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.all
import pandoro.composeapp.generated.resources.empty_filtered_groups
import pandoro.composeapp.generated.resources.groups
import pandoro.composeapp.generated.resources.my_groups
import pandoro.composeapp.generated.resources.role

/**
 * The [GroupsScreen] displays the groups lists of the [com.tecknobit.pandoro.localUser] such his/her
 * owned groups and the groups where him/she is a member
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see com.tecknobit.equinoxcompose.helpers.session.EquinoxScreen
 * @see PandoroScreen
 * @see ListsScreen
 */
class GroupsScreen : ListsScreen<GroupsScreenViewModel>(
    viewModel = GroupsScreenViewModel(),
    screenTitle = Res.string.groups
) {

    /**
     * Custom action to execute when the [androidx.compose.material3.FloatingActionButton] is clicked
     */
    @Composable
    @NonRestartableComposable
    override fun FabAction() {
        FloatingActionButton(
            onClick = { navigator.navigate(CREATE_GROUP_SCREEN) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }

    /**
     * The horizontal list to display the items in row format, the owned groups by the
     * [com.tecknobit.pandoro.localUser]
     */
    @Composable
    override fun ItemsInRow() {
        var groupsAvailable by remember { mutableStateOf(false) }
        if(groupsAvailable || viewModel.myGroupsStateFilters.value.isNotEmpty()) {
            SectionHeader(
                header = Res.string.my_groups,
                isAllItemsFiltering = false
            )
        }
        PaginatedLazyRow(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel.myGroupsState,
            contentPadding = PaddingValues(
                vertical = if (groupsAvailable)
                    10.dp
                else
                    0.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            firstPageEmptyIndicator = {
                groupsAvailable = false
                if(viewModel.myGroupsStateFilters.value.isNotEmpty()) {
                    EmptyResultWithFilters(
                        info = Res.string.empty_filtered_groups
                    )
                }
            },
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewHorizontalPageProgressIndicator() }
        ) {
            items(
                items = viewModel.myGroupsState.allItems!!,
                key = { group -> group.id }
            ) { group ->
                groupsAvailable = true
                MyGroupCard(
                    viewModel = viewModel,
                    group = group
                )
            }
        }
    }

    /**
     * The column or grid list dynamically adapted based on the screen size, the groups where the
     * [com.tecknobit.pandoro.localUser] is a member
     */
    @Composable
    @NonRestartableComposable
    override fun ItemsAdaptedSize() {
        SectionHeader(
            header = Res.string.all,
            isAllItemsFiltering = true
        )
        Groups(
            viewModel = viewModel
        )
    }

    /**
     * UI to filter the [ItemsInRow] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    @NonRestartableComposable
    override fun FilterRowItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterGroups(
            show = show,
            isAllProjectsFiltering = false,
            viewModel = viewModel
        )
    }

    /**
     * UI to filter the [ItemsAdaptedSize] list
     *
     * @param show Whether the dialog is shown
     */
    @Composable
    @NonRestartableComposable
    override fun FilterAllItemsUi(
        show: MutableState<Boolean>
    ) {
        FilterGroups(
            show = show,
            isAllProjectsFiltering = true,
            viewModel = viewModel,
            extraFilters = {
                Text(
                    modifier = Modifier
                        .padding(
                            top = 16.dp
                        ),
                    text = stringResource(Res.string.role),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                LazyColumn {
                    items(
                        items = Role.entries,
                    ) { role ->
                        var selected by remember {
                            mutableStateOf(viewModel.roleFilters.contains(role))
                        }
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    selected = it
                                    viewModel.manageRoleFilter(
                                        role = role
                                    )
                                }
                            )
                            Text(
                                text = role.asText()
                            )
                        }
                    }
                }
            },
            filterNameRequired = false,
            onDismissAction = { viewModel.resetRoles() }
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel.myGroupsStateFilters = remember { mutableStateOf("") }
        viewModel.allGroupsStateFilters = remember { mutableStateOf("") }
    }

}