package com.tecknobit.pandoro.ui.screens.lists.projects.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.pandoro.ui.screens.lists.projects.presentation.ProjectsScreenViewModel
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.filter_in_development_projects
import pandoro.composeapp.generated.resources.filter_projects
import pandoro.composeapp.generated.resources.filters_placeholder
import pandoro.composeapp.generated.resources.wrong_filters_text

/**
 * Dialog layout to allow the user to filter a projects list
 *
 * @param show Whether the dialog is shown
 * @param isAllProjectsFiltering Whether is filtering the complete projects list
 * @param viewModel The support viewmodel for the screen
 */
@Composable
fun FilterProjects(
    show: MutableState<Boolean>,
    isAllProjectsFiltering: Boolean,
    viewModel: ProjectsScreenViewModel
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
            Res.string.filter_projects
        else
            Res.string.filter_in_development_projects,
        text = {
            EquinoxOutlinedTextField(
                value = filters,
                isError = filtersError,
                label = "",
                placeholder = stringResource(Res.string.filters_placeholder),
                validator = { it.isNotEmpty() },
                errorText = stringResource(Res.string.wrong_filters_text)
            )
        },
        onDismissAction = closeFilteringOpe,
        confirmAction = {
            if(filters.value.isNotEmpty()) {
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