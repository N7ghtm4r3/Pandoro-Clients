package com.tecknobit.pandoro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.retrieving_data

/**
 * The custom progress indicator visible when the first page of the items requested has been loading
 */
@Composable
fun FirstPageProgressIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(85.dp),
            strokeWidth = 8.dp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier
                .padding(
                    top = 10.dp
                ),
            text = stringResource(Res.string.retrieving_data),
            fontSize = 14.sp
        )
    }
}

/**
 * The custom progress indicator visible when a new page of items has been requested
 */
@Composable
@NonRestartableComposable
fun NewPageProgressIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

/**
 * The custom progress indicator visible when a new page of items has been requested
 */
@Composable
@NonRestartableComposable
fun NewHorizontalPageProgressIndicator(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(35.dp),
            strokeWidth = 4.dp,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}