@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.group.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.tecknobit.pandoro.ui.screens.project.components.GroupProjectsCandidate
import com.tecknobit.pandoro.ui.screens.shared.viewmodels.groups.GroupManagerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@NonRestartableComposable
fun AttachProjectsButton(
    viewModel: GroupManagerViewModel
) {
    val projectsSheetState = rememberModalBottomSheetState()
    val projectsScope = rememberCoroutineScope()
    SmallFloatingActionButton(
        onClick = {
            projectsScope.launch {
                projectsSheetState.show()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.CreateNewFolder,
            contentDescription = null
        )
    }
    GroupProjects(
        modalBottomSheetState = projectsSheetState,
        scope = projectsScope,
        viewModel = viewModel
    )
}

@Composable
@NonRestartableComposable
private fun GroupProjects(
    modalBottomSheetState: SheetState,
    scope: CoroutineScope,
    viewModel: GroupManagerViewModel
) {
    GroupProjectsCandidate(
        modalBottomSheetState = modalBottomSheetState,
        scope = scope,
        projects = remember { viewModel.userProjects + viewModel.groupProjects },
        trailingContent = { project ->
            if (viewModel.userProjects.contains(project)) {
                var added by remember {
                    mutableStateOf(viewModel.candidateProjects.contains(project.id))
                }
                Checkbox(
                    checked = added,
                    onCheckedChange = { selected ->
                        viewModel.manageProjectCandidate(
                            project = project,
                            added = selected
                        )
                        added = selected
                    }
                )
            }
        }
    )
}