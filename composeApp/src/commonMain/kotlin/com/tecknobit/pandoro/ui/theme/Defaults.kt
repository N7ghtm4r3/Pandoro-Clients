package com.tecknobit.pandoro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

val EmptyStateTitleStyle
    @Composable get() = AppTypography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onPrimary
    )