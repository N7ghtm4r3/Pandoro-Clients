package com.tecknobit.pandoro.ui.components

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pandoro.composeapp.generated.resources.Res
import pandoro.composeapp.generated.resources.retry_to_reconnect

@Composable
fun RetryButton(
    onRetry: () -> Unit,
    text: StringResource = Res.string.retry_to_reconnect
) {
    TextButton(
        colors = ButtonDefaults.textButtonColors(
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onRetry
    ) {
        Text(
            text = stringResource(text)
        )
    }
}