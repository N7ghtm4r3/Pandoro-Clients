@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.pandoro.ui.screens.projects.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxTextField
import com.tecknobit.pandoro.ui.screens.projects.presentation.ProjectsScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@NonRestartableComposable
fun FilterProjects(
    viewModel: ProjectsScreenViewModel,
    state: SheetState,
    scope: CoroutineScope
) {
    if(state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Filter",
                textAlign = TextAlign.Center
            )
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(
                        all = 16.dp
                    )
            ) {
                EquinoxTextField(
                    value = remember { mutableStateOf("") },
                    label = ""
                )
            }
        }
    }
}