package com.tecknobit.pandoro.ui.screens.project.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.utilities.BorderToColor
import com.tecknobit.equinoxcompose.utilities.colorOneSideBorder
import com.tecknobit.pandoro.displayFontFamily
import com.tecknobit.pandoro.helpers.TimeFormatter.formatAsTimeString
import com.tecknobit.pandoro.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.pandoro.ui.screens.projects.data.Project.Companion.asVersionText
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate
import com.tecknobit.pandoro.ui.screens.projects.data.ProjectUpdate.Companion.toColor
import com.tecknobit.pandorocore.enums.UpdateStatus.IN_DEVELOPMENT
import com.tecknobit.pandorocore.enums.UpdateStatus.PUBLISHED
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.creation_date
import pandoro.composeapp.generated.resources.publish_date
import pandoro.composeapp.generated.resources.start_date

@Composable
@NonRestartableComposable
fun UpdateCard(
    modifier: Modifier,
    viewModel: ProjectScreenViewModel,
    update: ProjectUpdate
) {
    val status = update.status
    Card(
        modifier = modifier
            .fillMaxSize()
            .colorOneSideBorder(
                borderToColor = BorderToColor.END,
                color = update.status.toColor(),
                width = 10.dp,
                shape = CardDefaults.shape
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            CardHeader(
                update = update
            )
            UpdateDateInfo(
                header = Res.string.creation_date,
                dateValue = update.createDate
            )
            UpdateDateInfo(
                header = Res.string.start_date,
                dateValue = update.startDate,
                showIf = status == IN_DEVELOPMENT || status == PUBLISHED
            )
            UpdateDateInfo(
                header = Res.string.publish_date,
                dateValue = update.publishDate,
                showIf = status == PUBLISHED
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun CardHeader(
    update: ProjectUpdate
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = update.targetVersion.asVersionText(),
            fontFamily = displayFontFamily,
            fontSize = 20.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {
                    // TODO: ACTION MENU
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun UpdateDateInfo(
    header: StringResource,
    dateValue: Long,
    showIf: Boolean = true
) {
    if(showIf) {
        Column {
            Text(
                text = stringResource(header),
                fontSize = 12.sp
            )
            Text(
                text = dateValue.formatAsTimeString()
            )
        }
    }
}