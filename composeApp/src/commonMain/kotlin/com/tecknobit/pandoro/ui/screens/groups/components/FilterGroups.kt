package com.tecknobit.pandoro.ui.screens.groups.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.pandoro.ui.screens.groups.presentation.GroupsScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.filter_groups
import pandoro.composeapp.generated.resources.filter_my_groups
import pandoro.composeapp.generated.resources.group_filter_placeholder
import pandoro.composeapp.generated.resources.name
import pandoro.composeapp.generated.resources.wrong_filters_text

/**
 * Dialog layout to allow the user to filter a groups list
 *
 * @param show Whether the dialog is shown
 * @param isAllProjectsFiltering Whether is filtering the complete groups list
 * @param viewModel The support viewmodel for the screen
 * @param extraFilters Additive filters to use
 * @param filterNameRequired Whether the filter for the name is required
 * @param onDismissAction The action to execute when the dialog is dismissed
 */
@Composable
fun FilterGroups(
    show: MutableState<Boolean>,
    isAllProjectsFiltering: Boolean,
    viewModel: GroupsScreenViewModel,
    extraFilters: (@Composable () -> Unit)? = null,
    filterNameRequired: Boolean = true,
    onDismissAction: (() -> Unit)? = null
) {
    val filters = remember { mutableStateOf("") }
    val filtersError = remember { mutableStateOf(false) }
    val closeFilteringOpe =  {
        filters.value = ""
        filtersError.value = false
        show.value = false
    }
    EquinoxAlertDialog(
        icon = Icons.Default.FilterList,
        show = show,
        title = if(isAllProjectsFiltering)
            Res.string.filter_groups
        else
            Res.string.filter_my_groups,
        text = {
            Column {
                EquinoxOutlinedTextField(
                    value = filters,
                    isError = filtersError,
                    label = stringResource(Res.string.name),
                    placeholder = stringResource(Res.string.group_filter_placeholder),
                    validator = if(filterNameRequired) {
                        {
                            it.isNotEmpty()
                        }
                    } else
                        null,
                    errorText = stringResource(Res.string.wrong_filters_text)
                )
                extraFilters?.invoke()
            }
        },
        onDismissAction = if(onDismissAction != null)
        {
            {
                onDismissAction.invoke()
                closeFilteringOpe.invoke()
            }
        } else
            closeFilteringOpe,
        confirmAction = {
            if(!filterNameRequired || filters.value.isNotEmpty()) {
                viewModel.filterItems(
                    allItems = isAllProjectsFiltering,
                    filters = filters,
                    onFiltersSet = closeFilteringOpe
                )
            } else
                filtersError.value = true
        }
    )
}