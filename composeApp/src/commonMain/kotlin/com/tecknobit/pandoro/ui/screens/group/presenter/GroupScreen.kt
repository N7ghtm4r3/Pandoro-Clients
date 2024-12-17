package com.tecknobit.pandoro.ui.screens.group.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.collectAsState
import com.tecknobit.pandoro.CREATE_GROUP_SCREEN
import com.tecknobit.pandoro.navigator
import com.tecknobit.pandoro.ui.components.DeleteGroup
import com.tecknobit.pandoro.ui.screens.group.presentation.GroupScreenViewModel
import com.tecknobit.pandoro.ui.screens.groups.data.Group
import com.tecknobit.pandoro.ui.screens.project.components.ProjectIcons
import com.tecknobit.pandoro.ui.screens.shared.data.PandoroUser
import com.tecknobit.pandoro.ui.screens.shared.screens.ItemScreen

class GroupScreen(
    groupId: String
) : ItemScreen<Group, GroupScreenViewModel>(
    viewModel = GroupScreenViewModel(
        groupId = groupId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun ItemTitle() {
        ScreenTitle(
            navBackAction = { navigator.goBack() },
            title = item.value!!.name
        )
    }

    override fun getThumbnailData(): String {
        return item.value!!.logo
    }

    @Composable
    @NonRestartableComposable
    override fun ItemRelationshipItems() {
        ProjectIcons(
            group = item.value!!
        )
    }

    override fun getItemDescription(): String {
        return item.value!!.description
    }

    override fun onEdit() {
        navigator.navigate("$CREATE_GROUP_SCREEN/${item.value!!.id}")
    }

    @Composable
    @NonRestartableComposable
    override fun DeleteItemAction(
        delete: MutableState<Boolean>
    ) {
        DeleteGroup(
            viewModel = viewModel!!,
            group = item.value!!,
            show = delete,
            onDelete = {
                delete.value = false
                navigator.goBack()
            }
        )
    }

    override fun getItemAuthor(): PandoroUser {
        return item.value!!.author
    }

    @Composable
    override fun ScreenContent() {
    }

    @Composable
    @NonRestartableComposable
    override fun FabAction() {
    }

    override fun onStart() {
        super.onStart()
        viewModel!!.retrieveGroup()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
        item = viewModel!!.group.collectAsState()
    }

}